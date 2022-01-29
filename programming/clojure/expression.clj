(defn proto-get
  [obj key]
  (cond
    (contains? obj key) (obj key)
    (contains? obj :prototype) (proto-get (obj :prototype) key)
    :else nil))
(defn proto-call
  [this key & args]
  (apply (proto-get this key) this args))

(defn field
  [key]
  (fn [this] (proto-get this key)))
(defn method
  [key]
  (fn [this & args] (apply proto-call this key args)))

(defn constructor
  [ctor prototype]
  (fn [& args] (apply ctor {:prototype prototype} args)))

(defn assoc-constructor
  [prototype & fields]
  (constructor (fn [this & values]
                 (reduce (fn [this [field value]]
                           (assoc this
                                  field value))
                         this
                         (map vector fields values)))
               prototype))

(def evaluate (method :evaluate))
(def toString (method :toString))
(def toStringSuffix (method :toStringSuffix))
(def toStringInfix (method :toStringInfix))
(def diff (method :diff))
(def op-name (field :name))

(declare ZERO)
(declare ONE)
(declare TWO)

(def Constant-prototype
  (let [_value (field :value)]
    {:evaluate (fn [this _] (_value this))
     :toString (fn [this] (format "%.1f" (double (_value this))))
     :toStringSuffix (fn [this] (toString this))
     :toStringInfix (fn [this] (toString this))
     :diff (fn [_ _] ZERO)}))

(def Constant (assoc-constructor Constant-prototype :value))

(def ZERO (Constant 0))
(def ONE (Constant 1))
(def TWO (Constant 2))
(def E (Constant Math/E))

(def Variable-prototype
  (let [_name (field :name)]
    {:evaluate (fn [this values] (values (_name this)))
     :toString (fn [this] (_name this))
     :toStringSuffix (fn [this] (toString this))
     :toStringInfix (fn [this] (toString this))
     :diff (fn [this diffVarName]
             (if (= (_name this) diffVarName)
               ONE
               ZERO))}))

(def Variable (assoc-constructor Variable-prototype :name))

(def Operator-prototype
  (let [_f (field :f)
        _diffRule (field :diffRule)
        _name (field :name)
        _exprs (field :exprs)
        _toStringExprs (method :toStringExprs)]
    {:evaluate (fn [this values] (apply (_f this) (map #(evaluate % values) (_exprs this))))
     :toStringExprs (fn [this] (clojure.string/join " " (map toString (_exprs this))))
     :toString (fn [this] (str "(" (_name this) " "
                               (_toStringExprs this)
                               ")"))
     :toStringSuffix (fn [this] (str "(" (_toStringExprs this)
                                     " " (_name this)
                                     ")"))
     :toStringInfix (fn [this] (let [exprs (map toStringInfix (_exprs this))]
                                 (if (== (count exprs) 1) (str (_name this) "(" (first exprs) ")")
                                     (str "(" (first exprs) " "
                                          (_name this)
                                          " " (second exprs)
                                          ")"))))
     :diff (fn [this diffVarName] ((_diffRule this)
                                   (_exprs this)
                                   (mapv #(diff % diffVarName) (_exprs this))))}))

(defn do-assoc
  [this & values]
  (assoc this
         :exprs values))

(defn operator-constructor
  [f name diffRule]
  (constructor do-assoc
               ((assoc-constructor Operator-prototype :f :name :diffRule)
                f name diffRule)))

(declare Multiply)
(declare Divide)

(def Add (operator-constructor
          +
          "+"
          (fn [fs dfs] (apply Add dfs))))

(def Subtract (operator-constructor
               -
               "-"
               (fn [fs dfs] (apply Subtract dfs))))

(def Negate (operator-constructor
             -
             "negate"
             (fn [_ [df]] (Negate df))))

(defn multiply-diff-rule
  [fs dfs]
  (second (reduce (fn [[f df] [g dg]]
                    [(Multiply f g)
                     (Add (Multiply f dg)
                          (Multiply df g))])
                  (map vector fs dfs))))

(defn divide-diff-rule
  [[f & fs] [df & dfs]]
  (if (== (count fs) 0)
    (Divide (Negate df)
            (Multiply f f))
    (let [g (apply Multiply fs)
          dg (multiply-diff-rule fs dfs)]
      (Divide
       (Subtract (Multiply df g)
                 (Multiply f dg))
       (Multiply g g)))))

(def Multiply (operator-constructor
               *
               "*"
               multiply-diff-rule))

(def Divide (operator-constructor
             (fn
               ([x] (/ (double x)))
               ([x & other] (/ x (double (apply * other)))))
             "/"
             divide-diff-rule))

(def Sum (operator-constructor
          +
          "sum"
          (fn [fs dfs] (apply Sum dfs))))

(def Avg (operator-constructor
          #(/ (apply + %&) (count %&))
          "avg"
          (fn [fs dfs] (Divide (apply Add dfs)
                               (Constant (count fs))))))

(def Log (operator-constructor
          #(/ (Math/log (Math/abs %2)) (Math/log (Math/abs %1)))
          "//"
          (fn [[f g] [df dg]]
            (divide-diff-rule [(Log Math/E g)
                               (Log Math/E f)]
                              [(Divide dg g)
                               (Divide df f)]))))

(def Pow (operator-constructor
          #(Math/pow %1 %2)
          "**"
          (fn [[f g] [df dg]]
            (Multiply (Pow f g)
                      (Add (Multiply dg (Log E f))
                           (Multiply g (Divide df f)))))))

(def object-operations
  {'+ Add
   '- Subtract
   '* Multiply
   '/ Divide
   '** Pow
   (symbol "//") Log
   'negate Negate})


(defn parse-expression [operations expression constant variable]
  (cond
    (seq? expression) (apply (operations (first expression))
                             (mapv (partial parse-expression operations) (rest expression)))
    (number? expression) (constant expression)
    (symbol? expression) (variable (str expression))))

(defn common-parse
  [operations constant variable]
  (fn [expression]
    (parse-expression operations constant variable
                      (binding [*read-eval* false]
                        (read-string expression)))))

(def parseObject (common-parse object-operations Constant Variable))

;; -------------------------------------------------------

(defn -return [value tail] {:value value :tail tail})
(def -valid? boolean)
(def -value :value)
(def -tail :tail)
(defn _empty [value] (partial -return value))

(defn _char [p]
  (fn [[c & cs]]
    (if (and c (p c)) (-return c cs))))
(defn _map [f result]
  (if (-valid? result)
    (-return (f (-value result)) (-tail result))))

(defn _combine [f a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar)
        (_map (partial f (-value ar))
              ((force b) (-tail ar)))))))

(defn _either [a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar) ar ((force b) str)))))

(defn _parser [p]
  (fn [input]
    (-value ((_combine (fn [v _] v) p (_char #{\u0000})) (str input \u0000)))))

(defn +char "single character in string"
  [chars] (_char (set chars)))
(defn +char-not "single character not in string"
  [chars] (_char (comp not (set chars))))
(defn +map [f parser]
  (comp (partial _map f) parser))
(def +ignore "ignore result"
  (partial +map (constantly 'ignore)))
(defn iconj [coll value]
  (if (= value 'ignore) coll (conj coll value)))
(defn +seq "sequence"
  [& ps] (reduce (partial _combine iconj) (_empty []) ps))
(defn +seqf "sequence with function"
  [f & ps] (+map (partial apply f) (apply +seq ps)))
(defn +seqn "n-th element of the sequence"
  [n & ps] (apply +seqf (fn [& vs] (nth vs n)) ps))
(defn +or "first matching parser"
  [p & ps] (reduce _either p ps))
(defn +opt "Optional parser"
  [p] (+or p (_empty nil)))
(defn +star "Kleene star"
  [p] (letfn [(rec [] (+or (+seqf cons p (delay (rec))) (_empty ())))] (rec)))
(defn +plus "Kleene plus"
  [p] (+seqf cons p (+star p)))
(defn +str "convert to string"
  [p] (+map (partial apply str) p))
(defn +string [chars]
  (+str (apply +seq (map (comp +char str) (seq chars)))))

(def *digit (+char (filter #(Character/isDigit %) (map char (range 1 128)))))
(def *number (+map (comp Constant read-string)
                   (+seqf str
                          (+opt (+char "-"))
                          (+str (+plus *digit))
                          (+str (+opt (+seqf cons
                                             (+char ".")
                                             (+star *digit)))))))
(def *space (+char " \t\n\r"))
(def *ws (+ignore (+star *space)))
(def *needed-ws (+ignore (+plus *space)))
(def *variable (+map (comp Variable str) (+char "xyz")))
(defn *operator [ops]
  (apply +or (map #(+map (constantly %)
                         (+string (op-name (%))))
                  ops)))

(declare *suffix)
(def *list (+map (fn [[args op]] (apply op args))
                 (+seq (+ignore (+char "("))
                       (+plus (delay *suffix))
                       *ws
                       (*operator (vals object-operations))
                       *ws
                       (+ignore (+char ")"))
                       *ws)))
(def *suffix (+seqn 0 *ws (+or *number *variable *list) *ws))
(def parseObjectSuffix (_parser *suffix))

;; ------------------ INFIX --------------------

(defn *read-left [*op *next-level]
  (+seqf #(reduce (fn [a [op b]] (op a b)) %1 %2)
         *ws *next-level (+star (+seq *ws *op *ws *next-level))))

(defn *read-right [*op *next-level]
  (+seqf #(reduce (fn [a [b op]] (op b a)) %2 (reverse %1))
         (+star (+seq *ws *next-level *ws *op)) *ws *next-level))

(defn binary [reader & ops] {:ops ops
                             :reader reader})
(def binary-priorities
  [(binary *read-left Add Subtract)
   {:ops [Multiply Divide]
    :reader *read-left}
   {:ops [Pow Log]
    :reader *read-right}])

(def unary-ops [Negate])

(declare *infix-value)
(def *wrapped (+seqn 0 (+ignore (+char "(")) *ws (delay *infix-value) *ws (+ignore (+char ")"))))
(def *primary (+or *wrapped *number *variable))

(def parse-unary (+or *primary
                      (+seqf #(%1 %2) (*operator unary-ops) *ws (delay parse-unary))))

(defn parse-binary [priorities]
  (let [ops (:ops (first priorities))
        reader (:reader (first priorities))]
    (if (empty? ops)
      parse-unary
      (reader (*operator ops) (parse-binary (rest priorities))))))

(def *infix-value (+seqn 0 *ws (parse-binary binary-priorities) *ws))
(defn parseObjectInfix [in]
  (evaluate ((_parser *infix-value) in) {}))

;; -------------------------------------------------------

(def constant constantly)
(defn variable [name] #(% name))

(defn nary [f]
  (fn [& args]
    (fn [vars]
      (apply f ((apply juxt args) vars)))))

(def add (nary +))
(def subtract (nary -))
(def multiply (nary *))
(def divide (nary (fn
                    ([x] (/ (double x)))
                    ([x & other] (/ x (double (apply * other)))))))

(def negate (nary -))
(def med (nary #(nth (sort %&) (quot (count %&) 2))))
(def avg (nary #(/ (apply + %&) (count %&))))

(def function-operations
  {'+ add
   '- subtract
   '* multiply
   '/ divide
   'negate negate
   'med med
   'avg avg})

(def parseFunction (common-parse function-operations constant variable))

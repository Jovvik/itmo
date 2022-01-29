(defn my-vector?
  [v]
  (and (vector? v)
       (every? number? v)))

(defn equal-size?
  [& objs]
  (or (every? number? objs)
      (and (every? vector? objs)
           (apply == (mapv count objs))
           (every? true? (apply mapv equal-size? objs)))))

(defn matrix?
  [m]
  (and (vector? m)
       (every? my-vector? m)
       (apply equal-size? m)))

(defn vector-by-coord
  [f]
  (fn [& vs]
    {:pre [(every? my-vector? vs)
           (apply equal-size? vs)]
     :post [(my-vector? %)
            (equal-size? % (first vs))]}
    (apply mapv f vs)))

(def v+ (vector-by-coord +))
(def v- (vector-by-coord -))
(def v* (vector-by-coord *))
(def vd (vector-by-coord /))

(defn scalar
  [& vs]
  {:pre [(every? vector? vs)
         (apply equal-size? vs)]
   :post [(number? %)]}
  (apply + (apply v* vs)))

(defn vect
  [& vs]
  {:pre [(every? my-vector? vs)
         (apply equal-size? vs)
         (== (count (first vs)) 3)]
   :post [(my-vector? %)
          (== (count %) 3)]}
  (reduce (fn [v1 v2]
            [(- (* (v1 1) (v2 2)) (* (v1 2) (v2 1)))
             (- (* (v1 2) (v2 0)) (* (v1 0) (v2 2)))
             (- (* (v1 0) (v2 1)) (* (v1 1) (v2 0)))])
          vs))

(defn v*s
  [v & ss]
  {:pre [(my-vector? v)
         (every? number? ss)]
   :post [(my-vector? %)
          (equal-size? v %)]}
  (mapv (partial * (apply * ss)) v))

(defn transpose
  [m]
  {:pre [(matrix? m)]
   :post [(matrix? %)
          (== (count %) (count (m 0)))
          (== (count (% 0)) (count m))]}
  (apply mapv vector m))

(defn matrix-by-coord
  [f]
  (fn [& ms]
    {:pre [(every? matrix? ms)
           (apply equal-size? ms)]
     :post [(matrix? %)
            (equal-size? % (first ms))]}
    (apply mapv (vector-by-coord f) ms)))

(def m+ (matrix-by-coord +))
(def m- (matrix-by-coord -))
(def m* (matrix-by-coord *))
(def md (matrix-by-coord /))

(defn m*s [m & ss]
  {:pre [(every? number? ss)
         (matrix? m)]
   :post [(matrix? %)
          (equal-size? % m)]}
  (let [ss-multipled (apply * ss)]
    (mapv (fn [v] (v*s v ss-multipled)) m)))

(defn m*v [m & vs]
  {:pre [(every? my-vector? vs)
         (matrix? m)
         (apply equal-size? (first m) vs)]
   :post [(vector? %)
          (= (count %) (count m))]}
  (mapv (partial scalar (apply v* vs)) m))

(defn m*m [& ms]
  {:pre [(every? matrix? ms)
         (equal-size? (mapv first ms))]
   :post [(matrix? %)
          (== (count %) (count (first ms)))
          (== (count (first %)) (count (first (last ms))))]}
  (reduce (fn [m1 m2] (mapv (partial m*v (transpose m2)) m1)) ms))

(defn tensor?
  [t]
  (or (every? number? t)
      (and (every? vector? t)
           (apply == (mapv count t))
           (every? tensor? (apply mapv vector t)))))

(defn tensor-by-coord
  [f]
  (fn operate [& ts]
    {:pre [(every? tensor? ts)
           (apply equal-size? ts)]
     :post [(tensor? %)
            (equal-size? % (first ts))]}
    (if (number? (first ts))
      (apply f ts)
      (apply mapv operate ts))))

(def t+ (tensor-by-coord +))
(def t- (tensor-by-coord -))
(def t* (tensor-by-coord *))
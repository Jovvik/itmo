package search;

public class BinarySearchSpan {
    // a[-1] := ∞, a[a.length] := -∞
    // sorted(arr) := (arr.length == 0) || (∀ i = 1..arr.length-1 : arr[i] <= arr[i - 1])

    // x := (int)args[0]
    // ((int)args[R1] <= x < (int)args[R1 - 1]) ∧ ((int)args[R2 + 1] < x <= (int)args[R2])
    // R3 := R2 - R1 + 1
    // Pre: args.length > 0 ∧ (∀ i = 0..args.length-1 args[i].isInteger) ∧ sorted(args[1..args.length - 1])
    // Post: out -> "R1 R3"
    public static void main(final String[] args) {
        // Pre
        final int x = Integer.parseInt(args[0]);
        // Pre ∧ x = (int)args[0]
        final int[] a = new int[args.length - 1];
        // Pre ∧ x = (int)args[0] ∧ (∀ i = 0..args.length - 1 : a[i] = 0)
        for (int i = 1; i < args.length; i++) {
            a[i - 1] = Integer.parseInt(args[i]);
        }
        // Pre ∧ x = args[0] ∧ (a = [(int)args[1]..(int)args[args.length - 1]])
        int left = recursiveSearchLeft(a, x, -1, a.length);
        // left = R1
        int right = iterativeSearchRight(a, x);
        // right = R2
        int len = right - left + 1;
        // len = R2 - R1 + 1 = R3
        System.out.println(left + " " + len);
        // out -> "R1 R3"
    }

    // Pre := (-1 <= left < right <= a.length) ∧ sorted([a[left]..a[right]]) ∧ (a[right] <= x < a[left])
    // Var := right - left (верхняя оценка на глубину рекурсии)
    // Post := a[R] <= x < a[R - 1]
    public static int recursiveSearchLeft(final int[] a, int x, int left, int right) {
        // Pre
        if (right - 1 == left) {
            // Pre ∧ right - 1 = left
            // Проверка Post: (a[right] <= x) ∧ (a[right - 1] = a[left] > x) 
            return right;
        }
        // Pre ∧ right - 1 != left
        final int middle = (left + right) / 2;
        // (right - 1 != left) ∧ (left < right) => left + 2 <= right => left < middle < right
        // Pre ∧ (left + 1 < right) ∧ (left < middle < right)
        if (a[middle] <= x) {
            // Pre ∧ (left + 1 < right) ∧ (left < middle < right) ∧ (a[middle] <= x)
            right = middle;
            // Проверка Pre для следующего вызова: (-1 <= left = left' < right' = middle < right <= a.length) ∧ (sorted([a[left]..a[right]]) => sorted([a[left']..a[right']])) ∧
            //   ∧ (a[right'] = a[middle] <= x < a[left] = a[left'])
            // Проверка Var: Var' = right' - left' = middle - left < right - left = Var => рекурсия конечна
            return recursiveSearchLeft(a, x, left, right);
        } else {
            // Pre ∧ (left + 1 < right) ∧ (left < middle < right) ∧ (x < a[middle])
            left = middle;
            // Проверка Pre для следующего вызова: (-1 <= left < middle = left' < right' = right <= a.length) ∧ (sorted([a[left]..a[right]]) => sorted([a[left']..a[right']])) ∧
            //   ∧ (a[right'] = a[right] <= x < a[middle] = a[left'])
            // Проверка Var: Var' = right' - left' = right - middle < right - left = Var => рекурсия конечна
            return recursiveSearchLeft(a, x, left, right);
        }
    }
    
    // Pre := sorted(a)
    // Post := a[R] < x <= a[R - 1]
    public static int iterativeSearchRight(final int[] a, final int x) {
        // Pre
        int left = -1, right = a.length;
        // Pre ∧ left = -1 ∧ right = a.length
        // Inv := (a[right] < x <= a[left]) ∧ (-1 <= left < right <= a.length) ∧ Pre
        // Var := right - left (верхняя оценка на число итераций цикла)
        while (right > left + 1) {
            // Inv ∧ (right > left + 1)
            final int middle = (left + right) / 2;
            // (right > left + 1) => (left < middle < right)
            // Inv ∧ (left < middle < right)
            if (a[middle] < x) {
                // Inv ∧ (left < middle < right) ∧ (a[middle] < x) ∧ (right > left + 1)
                right = middle;
                // Inv ∧ (left < middle < right) ∧ (a[middle] < x) ∧ (left' = left ∧ right' = middle) ∧ (right > left + 1)
                // right' - left' = middle - left < right - left
                // Проверка Inv: (a[right'] = a[middle] < x <= a[left'] = a[left]) ∧ (-1 <= left = left' < middle = right' < right <= a.length) ∧ Pre
            } else {
                // Inv ∧ (left < middle < right) ∧ (a[middle] >= x) ∧ (right > left + 1)
                left = middle;
                // Inv ∧ (left < middle < right) ∧ (a[middle] >= x) ∧ (right > left + 1) ∧ (left' = middle ∧ right' = right)
                // right' - left' = right - middle < right - left
                // Проверка Inv: (a[right'] = a[right] < x <= a[middle] = a[left]) ∧ (-1 <= left < middle = left' < right' = right <= a.length) ∧ Pre
            }
            // Inv ∧ right' - left' < right - left
            // Var' = right' - left' < right - left = Var => цикл конечен
        }
        // Inv ∧ right <= left + 1
        // (right <= left + 1) ∧ (left < right) => left + 1 = right
        // Проверка Post: a[left + 1] = a[right] < x <= a[left]
        return left;
    }
}
import java.util.Scanner;
public class ProceduralCalculator {
    public static double calculate(double a, String operator, double b) {
        return switch (operator) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> { if (b == 0) throw new ArithmeticException("Cannot divide by zero"); yield a / b; }
            default -> throw new IllegalArgumentException("Invalid operator: " + operator);
        };
    }
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("First number: "); double a = sc.nextDouble();
            System.out.print("Operator (+ - * /): "); String operator = sc.next();
            System.out.print("Second number: "); double b = sc.nextDouble();
            System.out.println("Result = " + calculate(a, operator, b));
        } catch (RuntimeException ex) { System.err.println("Input error: " + ex.getMessage()); }
    }
}
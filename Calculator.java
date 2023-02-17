import java.util.Scanner;

public class Calculator {
    private static String lastOperator;
    private static String operators = new String("+-*/");
    private static double result = 0;

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.calculate();
    }

    public void calculate() { //计算入口
        while(true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please enter number1:");
            String number1 = scanner.nextLine();
            if (!verifyNum(number1)) { // 校验和处理第一个数字
                System.out.println("The number is invalid:" + number1);
                continue;
            }
            double num1 = Double.valueOf(number1);

            System.out.println("Please enter operator(+-*/):");
            lastOperator = scanner.nextLine(); //校验和处理第操作符
            if (!verifyOperator()) {
                System.out.println("The operator is invalid:" + lastOperator);
                continue;
            }

            System.out.println("Please enter number2:");
            String number2 = scanner.nextLine(); // 校验和处理第二个数字
            if (!verifyNum(number2)) {
                System.out.println("The number is invalid:" + number2);
                continue;
            }

            if (lastOperator.equals("/") && number2.equals("0")) { // 防止除以0的情况
                System.out.println("Number 2 can not divide by zero:" + number2);
                continue;
            }

            double num2 = Double.valueOf(number2);
            if (!calculate(num1, num2)) {
                System.out.println("calculate error num1:" + num1 + ", num2:" + num2);
                continue;
            }

            System.out.println("The result is:" + result);
            retry(scanner, num2); // undo和redo的入口
        }
    }

    private void retry(Scanner scanner, double num2) {
        System.out.println("If need to redo/undo, please enter r/u. Otherwise, please enter enter any other keys.");
        String operator = scanner.nextLine();
        if (operator.equals("r")) {
            if (redo(num2)){
                System.out.println("The redo result is:" + result);
            } else {
                System.out.println("Fail to redo");
            }
        }

        if (operator.equals("u")) {
            if (undo(num2)) {
                System.out.println("The undo result is:" + result);
            } else {
                System.out.println("Fail to undo");
            }
        }
    }

    private boolean verifyNum(String number) {
        if (number.length() >= 15) { // 限制数字长度
            return false;
        }

        char[] chars = number.toCharArray();

        for (char ch: chars) {
            if (ch < '0' || ch > '9') {
                return false;
            }
        }

        return true;
    }

    private boolean verifyOperator() {
        return operators.contains(lastOperator);
    }

    private boolean calculate(double num1, double num2) {
        switch (lastOperator) {
            case "+":
                result = num1 + num2;
                return true;
            case "-":
                result = num1 - num2;
                return true;
            case "*":
                result = num1 * num2;
                return true;
            case "/":
                result = num1 / num2;
                return true;
            default:
                System.out.println("The operator is not valid:" + lastOperator);
        }

        return false;
    }

    private boolean rollback(double num1, double num2) {
        switch (lastOperator) {
            case "+":
                result = num1 - num2;
                return true;
            case "-":
                result = num1 + num2;
                return true;
            case "*":
                if (Double.compare(num2, 0) == 0) {
                    System.out.println("The operator is not valid:" + lastOperator);
                    return false;
                }
                result = num1 / num2;
                return true;
            case "/":
                result = num1 * num2;
                return true;
            default:
                System.out.println("The operator is not valid:" + lastOperator);
        }

        return false;
    }

    private boolean redo(double num2) {
        return calculate(result, num2);
    }

    private boolean undo(double num2) {
        return rollback(result, num2);
    }
}
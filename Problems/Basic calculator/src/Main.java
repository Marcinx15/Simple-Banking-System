class Problem {

    public static void main(String[] args) {
        String operator = args[0];
    
        int firstOperand = Integer.parseInt(args[1]);
        int secondOperand = Integer.parseInt(args[2]);

        switch (operator) {
            case "+":
                System.out.println(firstOperand + secondOperand);
                break;
            case "-":
                System.out.println(firstOperand - secondOperand);
                break;
            case "*":
                System.out.println(firstOperand * secondOperand);
                break;
            default:
                System.out.println("Unknown operator");
                break;
        }
    } 

}

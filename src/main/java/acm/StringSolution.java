package acm;

import java.util.Stack;

/**
 * @program: linuxtest
 * @description: 字符串相关结题
 * @author: YeDongYu
 * @create: 2020-03-09 13:45
 */
public class StringSolution {
    /**
     * @param s: an expression includes numbers, letters and brackets
     * @return: a string
     */
    public String expressionExpand(String s) {
        // write your code here
        if (null == s){
            return null;
        }
        Stack<StringBuilder> stringBuilderStack = new Stack<>();
        Stack<Integer> numsStack= new Stack<>();
        StringBuilder newStr = new StringBuilder();
        StringBuilder newNum = new StringBuilder();
        for (Character character:s.toCharArray()){
            if (character>47 && character<58){
                newNum.append(character);
            } else if (character.equals('[')){
                numsStack.push(Integer.valueOf(newNum.toString()));
                stringBuilderStack.push(newStr);
                newStr = new StringBuilder();
                newNum = new StringBuilder();
            } else if (character.equals(']')){
                StringBuilder pop = stringBuilderStack.pop();
                Integer n = numsStack.pop();
                while (n>0){
                    pop.append(newStr);
                    n--;
                }
                newStr = pop;
            } else {
                newStr.append(character);
            }
        }
        return newStr.toString();
    }

    public static void main(String[] args) {
        System.out.println("abc3[a]");
        System.out.println(new StringSolution().expressionExpand("ab10[c3[a]]"));
    }
}

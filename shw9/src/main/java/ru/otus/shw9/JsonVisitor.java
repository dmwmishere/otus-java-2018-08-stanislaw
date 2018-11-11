package ru.otus.shw9;

import java.util.Stack;

public class JsonVisitor implements Visitor{

    StringBuilder sb = new StringBuilder();

    String indent = " ";

    private final char ARRAY_BEGIN = '[';

    private final char ARRAY_END   = ']';

    private final char OBJECT_BEGIN = '{';

    private final char OBJECT_END   = '}';

    private int currentLevel = -1;

    private Stack<Character> stack = new Stack<>();

    public void visit(String name, Object value, int level) {
        printNode(name, value, level);
        Class clazz = value.getClass();
        if(currentLevel < level){
            sb.append(clazz.isArray()?ARRAY_BEGIN:OBJECT_BEGIN);
        }

        if(currentLevel > level){
            sb.append(clazz.isArray()?ARRAY_END:OBJECT_END);
        }

        sb.append(name).append(":").append(value.toString()).append(", ");

        currentLevel = level;
        System.out.println(getJson());
    }

    private void printNode(String name, Object value, int level){
        StringBuilder tmpIndent = new StringBuilder();
        for(int i = 0; i < level; i++){
            tmpIndent.append(indent);
        }
        System.out.println(tmpIndent.toString() + level +" " + name +" = " + value.getClass());


    }

    public String getJson(){
        return sb.toString();
    }
}

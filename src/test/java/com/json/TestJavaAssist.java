package com.json;

import org.apache.ibatis.javassist.*;

import java.io.IOException;

public class TestJavaAssist {
    /**
     * @param args
     */
    public static void main(String[] args) {
        //创建class池
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.makeClass("com.dasenlin.bean.Employ");
        //创建属性
        try {
            CtField f1 = CtField.make("private Integer empId;", cc);
            CtField f2 = CtField.make("private String empName;",cc);
            CtField f3 = CtField.make("private Integer empAge;", cc);

            cc.addField(f1);
            cc.addField(f2);
            cc.addField(f3);

            CtMethod m1=CtMethod.make("public Integer getEmpId(){return empId;}", cc);
            CtMethod m2=CtMethod.make("public void setEmpId(Integer empId){this.empId=empId;}", cc);

            cc.addMethod(m1);
            cc.addMethod(m2);

            CtConstructor constructor =new CtConstructor(new CtClass[]{CtClass.intType,pool.get("java.lang.String")},cc);
            constructor.setBody("{this.empId=empId;this.empName=empName;this.empAge=empAge;}");
            cc.addConstructor(constructor);

            cc.writeFile("E:/LearnJavaProjectText/myjava");
            System.out.println("利用东京大学更简单的操作java字节码文件的jar包javaAssist操作类成功");
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

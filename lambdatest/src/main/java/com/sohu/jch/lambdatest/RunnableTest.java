package com.sohu.jch.lambdatest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jch on 16/5/18.
 */
public class RunnableTest {

    public static void runLambdaTest(){

        System.out.println("=== RunnableTest ===");

        Runnable r1 = new Runnable() {
            @Override
            public void run() {

                System.out.println("Hello world one !");
            }
        };

        Runnable r2 = () -> System.out.println("Hello world two!");

        r1.run();
        r2.run();

        List<Persion> persions = new ArrayList<>();

        persions.add(new Persion(10, "add", 39));
        persions.add(new Persion(30, "remo", 90));
        persions.add(new Persion(24, "dad", 89));

    }


    public static class Persion{

        private int age;

        private String name;

        private int score;

        public Persion(int age, String name, int score) {
            this.age = age;
            this.name = name;
            this.score = score;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }
    }

}

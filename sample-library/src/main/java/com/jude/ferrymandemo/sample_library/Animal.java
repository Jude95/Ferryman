package com.jude.ferrymandemo.sample_library;

import java.util.Map;

/**
 * Created by Jude on 2017/12/1.
 */

public class Animal {
    String name;
    Map<String,Integer> attributes;
    int age;
    double speed;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Integer> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Integer> attributes) {
        this.attributes = attributes;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "name: "+name
                + "age: "+age
                + "speed: "+speed
                + "attributes: "+attributes;
    }
}

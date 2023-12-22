package com.example.printme.helpers;

import java.util.Random;

public enum WordList {
    Apple, Pear, Banana, Grape, Carrot, Tomato, Cat, Dog, Frog, Elephant, Monkey, Giraffe , House, Sun, Car, Clock, Book, Key ,Doctor, Teacher, Firefighter, Cook, Architect, Artist, Bicycle, Airplane, Ship, Train, Bus , Football, Basketball, Tennis, Swimming, Golf, Boxing , Cup, Tree, Flower, Umbrella, Computer, Guitar,
    Circle, Square, Triangle, Oval, Rectangle, Star, Cloud, Rain, Snow, Lightning, Wind;

    private static final WordList[] VALUES = values();
    private static final int SIZE = VALUES.length;
    private static final Random RANDOM = new Random();

    public static String getRandomWord() {
        return VALUES[RANDOM.nextInt(SIZE)].name().toLowerCase();
    }
    }

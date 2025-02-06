package com.example.blade;

import com.hellokaton.blade.Blade;

public class App 
{
    public static void main( String[] args )
    {
        try {
            Blade.create().start();
        } catch (Exception e) {
            System.out.println();
        }
    }
}

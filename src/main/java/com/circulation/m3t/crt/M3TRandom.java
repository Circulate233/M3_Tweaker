package com.circulation.m3t.crt;

import com.circulation.m3t.M3TCrtAPI;
import project.studio.manametalmod.MMM;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Random;

@ZenClass(M3TCrtAPI.CrtClass + "Random")
public class M3TRandom {

    public M3TRandom(){

    }

    public M3TRandom(Random random){
        this.random = random;
    }

    public Random random = MMM.rand;

    @ZenMethod
    public static M3TRandom getRandom(){
        return new M3TRandom();
    }

    @ZenMethod
    public int nextInt(){
        return random.nextInt();
    }

    @ZenMethod
    public int nextInt(int bound){
        return random.nextInt(bound);
    }

    @ZenMethod
    public float nextFloat(){
        return random.nextFloat();
    }

    @ZenMethod
    public float nextFloat(float max){
        return random.nextFloat() * max;
    }

    @ZenMethod
    public double nextDouble() {
        return random.nextDouble();
    }

    @ZenMethod
    public double nextDouble(double max) {
        if (max <= 0) throw new IllegalArgumentException("max must be positive");
        return random.nextDouble() * max;
    }

    @ZenMethod
    public long nextLong() {
        return random.nextLong();
    }

    @ZenMethod
    public long nextLong(long bound) {
        if (bound <= 0) throw new IllegalArgumentException("bound must be positive");

        long bits;
        long val;
        do {
            bits = random.nextLong() & Long.MAX_VALUE;
            val = bits % bound;
        } while (bits - val + (bound - 1) < 0);
        return val;
    }
}

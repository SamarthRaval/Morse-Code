package com.example.pauld.morsecode;

/**
 * MorseTrie
 *     This class is used by the MorseBrain class as a trie structure to alleviate the processing
 *     required to input Morse code. This majority of the logic required to instantiate a MorseTrie
 *     is present in the recursive MorseTrieBranch class constructor.
 *
 *     @author Paul Duchesne B00332119
 **/
public class MorseTrie {
    private MorseTrieBranch StartBranch;
    private MorseTrieBranch CurrentBranch;
    private MorseTrieBranch TailBranch;

    public MorseTrie() {
        // Recursively creates trie. Turtles all the way down.
        TailBranch = new MorseTrieBranch();
        StartBranch = new MorseTrieBranch(MorseCodeStandards.InternationalStandard[0], TailBranch);
        CurrentBranch = StartBranch;
    }

    public MorseTrieBranch InputDot() {
        CurrentBranch = CurrentBranch.DotBranch;
        return CurrentBranch;
    }

    public MorseTrieBranch InputDash() {
        CurrentBranch = CurrentBranch.DashBranch;
        return CurrentBranch;
    }

    public void ResetTrie() {
        CurrentBranch = StartBranch;
    }

    public MorseTrieBranch GetCurrentBranch() {
        return CurrentBranch;
    }

    public MorseTrieBranch GetStartBranch() {
        return StartBranch;
    }

}

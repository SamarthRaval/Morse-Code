package com.example.pauld.morsecode;

/**
 * MorseCodeStandards
 *     This class holds the International Morse code table, with each table entry holding the
 *     character, its Morse translation, and the next character it iterates to. This table is used
 *     by the MorseTrieBranch constructor class to recursively create a MorseTrie.
 *
 *     @author Paul Duchesne B00332119
 **/
public class MorseCodeStandards {

    // 0 -> ASCII Character
    // 1 -> Morse Code Characters
    // 2 -> Next Character if a DOT (•) is added
    // 3 -> Next Character if a DASH (-) is added
    public final static String InternationalStandard[][] = {
            {"",  "",      "E", "T"}, // Empty is actually a space
            {"A", "•-",    "R", "W"}, // 1st entry for start of letters
            {"B", "-•••",   "6", "?"},
            {"C", "-•-•",  "?", "?"},
            {"D", "-••",   "B", "X"},
            {"E", "•",     "I", "A"},
            {"F", "••-•",  "?", "?"},
            {"G", "--•",   "Z", "Q"},
            {"H", "••••",  "5", "4"},
            {"I", "••",    "S", "U"},
            {"J", "•---",  "?", "1"},
            {"K", "-•-",   "C", "Y"},
            {"L", "•-••",  "?", "?"},
            {"M", "--",    "G", "O"},
            {"N", "-•",    "D", "K"},
            {"O", "---",   " ", " "},
            {"P", "•--•",  "?", "?"},
            {"Q", "--•-",  "?", "?"},
            {"R", "•-•",   "L", "?"},
            {"S", "•••",   "H", "V"},
            {"T", "-",     "N", "M"},
            {"U", "••-",   "F", " "},
            {"V", "•••-",  "?", "3"},
            {"W", "•--",   "P", "J"},
            {"X", "-••-",  "?", "?"},
            {"Y", "-•--",  "?", "?"},
            {"Z", "--••",  "7", "?"},
            {"0", "-----", "?", "?"}, // 27th entry for start of numbers
            {"1", "•----", "?", "?"},
            {"2", "••---", "?", "?"},
            {"3", "•••--", "?", "?"},
            {"4", "••••-", "?", "?"},
            {"5", "•••••", "?", "?"},
            {"6", "-••••", "?", "?"},
            {"7", "--•••", "?", "?"},
            {"8", "---••", "?", "?"},
            {"9", "----•", "?", "?"},

            // Three special branches to lead to some numbers
            {" ", "••--", "?", "2"}, // Branch leading to 2         // Coming from U
            {" ", "---•", "8", "?"}, // Branch leading to 8         // Coming from O
            {" ", "----", "9", "0"}, // Branch leading to 9 && 0    // Coming from O

            // General Confusion branch (Linked to branches at the bottom of the Trie)
            {"?", "", "?", "?"}
    };
    private final static int InternationalLength = InternationalStandard.length;

    // MorseCodeStr -> Code that this character is coming FROM
    // DotBranch -> Whether this is looking for a dotBranch (true) or dashBranch (false)
    // Both of the above are just used to handle special cases
    public static int GetRow(char InputChar, String MorseCodeStr, boolean DotBranch) {
        if (InputChar == ' ') {
            // Special cases:
            if (MorseCodeStr.equals("••-")) return InternationalLength - 4;
            if (DotBranch && MorseCodeStr.equals("---")) return InternationalLength - 3;
            if (!DotBranch && MorseCodeStr.equals("---")) return InternationalLength - 2;

            // Else this is an error
            return -1;
        }

        if (InputChar == '?') {
            return -1;
        }

        return GetRowNotSpecial(InputChar);
    }

    public static int GetRowNotSpecial(char InputChar) {
        int InputInt = (int)(Character.toUpperCase(InputChar));

        // It's a number
        if (InputInt >= 48 && InputChar <= 57) {
            return InputInt - 48 + 27; // 48 for ASCII offset, 27 for table offset
        }
        // Else it's a letter
        else if (InputInt >= 65 && InputChar <= 90) {
            return InputInt - 65 + 1;
        }
        // Else it's an error input char
        else {
            // If invalid range, return error
            return -1;
        }
    }

    static public String GiveLetterGetMorse(String inputCharButActuallyString) {
        int tmpIndex = GetRowNotSpecial(Character.toUpperCase(inputCharButActuallyString.charAt(0)));

        if (tmpIndex != -1) return InternationalStandard[tmpIndex][1];
        else return "~";
    }
}

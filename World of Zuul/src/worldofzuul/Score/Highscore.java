/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package worldofzuul.score;

import java.util.List;

import java.io.File;
import java.io.PrintWriter;

import java.nio.file.Files;

import worldofzuul.misc.Directories;

/**
 *
 * @author Bytoft, Mikkel
 * @author Christensen, Martin Steen
 * @author Hansen, Søren Vest
 * @author Johansen, Emil Højgaard
 * @author Madsen, Kent vejrup
 * @author Thy, Mads Heimdal
 */
/**
 * An extended version of the highscoreSystem, that retrieves a bunch of users.
 */
public class Highscore extends HighscoreSystem {

    /**
     * Constructor
     */
    public Highscore() {

        // Check if directory Exist
        if (worldofzuul.IO.Directories.exist(new File(Directories.HighscoreTable)) == false) {
            // Create it
            worldofzuul.IO.Directories.create(Directories.HighscoreTable,
                    true);
        }

        
    }

    /**
     * Saves a character's, current score
     *
     * @param PlayerName
     * @param Score
     * @return True: Saved, False: Error occured
     */
    public static boolean saveCharacter(String PlayerName, int Score) {
        StringBuilder builder = new StringBuilder();

        builder.append(PlayerName);
        builder.append(',');
        builder.append(Integer.toString(Score));

        File file = new File(Directories.HighscoreTable + "\\" + PlayerName);

        try {
            PrintWriter pw = new PrintWriter(file, "UTF-8");

            pw.println(builder.toString());
            pw.close();
        } catch (Exception ex) {
            return false;
        }

        return true;
    }
    
    /**
     * Loads a characters current score 
     * @param PlayerName
     * @return 
     */
    public static HighscorePlayer loadCharacter(String PlayerName)
    {
        File userFile = new File( Directories.HighscoreTable + "\\" + PlayerName );
        HighscorePlayer player = new HighscorePlayer();
        
        try
        {
            String s = Highscore.getContent(userFile);
            String[] a = s.split(",");
            
            player.setPlayerName( a[0] );
            player.setPlayerScore( Integer.parseInt( a[1]) );
        }
        catch(Exception ex)
        {
            
        }
        
        return player;
    }

    /* 
    Note: Returns the file, line by line
    // If the file is large enough, it can cause OutOfMemoryError
    // And cause the program to crash, however. due to the fact that
    // we're only reading names and stuff, it wont be a huge problem. 
    */
    
    /**
     * Retrieves the last line (with a given character that is allowed)
     * @param userFile
     * @return 
     */
    private static String getContent( File userFile )
    {
        String retValue = "";
        
        try {
                List<String> linesRead = Files.readAllLines( userFile.toPath() );

                for ( String currentline : linesRead) 
                {
                    
                    if(hsText.isStringWhitespaceOrNull(currentline) == false)
                    {
                        retValue = currentline;
                    }
                    
                }

            } catch (java.lang.SecurityException SEx) {
                // Indicates a security violation.
                // Triggered...
            } catch (java.io.IOException IOEx) {
            } catch (java.lang.IllegalArgumentException IAEx) {
            } catch (java.lang.OutOfMemoryError OOMEx) {
            } catch (Exception Ex) {
            }
        
        return retValue;
    }
    
    /**
     * Loads other Character's that are saved
     */
    public final void loadPlayers() {
        // Directory for the highscore files
        File playerFilesDirectory = new File(Directories.HighscoreTable);

        // List of files
        File[] listedPlayerFiles = worldofzuul.IO.List.listFiles(playerFilesDirectory);

        // Reads each files, and adds them to the highscore list
        for ( File user : listedPlayerFiles ) 
        {
            String s = getContent(user);
            
            String[] result = s.split(",");
            
            addPlayers( result[0], Integer.parseInt( result[1] ) );
        }

    }

    // Functions -------------------------------------------------------------->
    private static class hsText {
        
        /**
         * 
         * @param s
         * @return 
         */
        public static boolean isStringWhitespaceOrNull( String s )
        {
            if(s.isEmpty())
                return true;
            
            for(char c : s.toCharArray())
            {
                if(isAlphabetic(c))
                    return false;
                
                if(isNumber(c))
                    return false;   
            }
            
            return true;
        }


        /**
         * is the character allowed.
         * @param inputValue
         * @return
         */
        public static boolean allowedCharacter(char inputValue) {
            if (isAlphabetic(inputValue)) {
                return true;
            }

            if (isNumber(inputValue)) {
                return true;
            }

            if (isSpace(inputValue)) {
                return true;
            }

            return false;
        } // End AllowedCharacter

        /**
         * checks if the current character input is a alphabetic character
         * @param Input
         * @return 
         */
        public static boolean isAlphabetic(char Input) {
            if (Input >= 'A'&& Input <= 'z') {
                return true;
            }

            return false;
        }

        /**
         * checks if the current input character is a number
         * @param Input
         * @return 
         */
        public static boolean isNumber(char Input) {
            if (Input >= '0' && Input <= '9') {
                return true;
            }

            return false;
        }

        /**
         * checks if the current character is space
         * @param Input
         * @return 
         */
        public static boolean isSpace(char Input) {
            // Usually - & _ can indicate it's space
            if (Input >= '-' || Input == '_') {
                return true;
            }

            return false;
        }

    } // End Parsing  

}  // End Class Main

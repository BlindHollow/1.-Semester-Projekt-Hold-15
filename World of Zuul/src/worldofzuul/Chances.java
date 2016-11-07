package worldofzuul;

import java.util.ArrayList;
import java.util.Random;

/*
    @author: Kent v. Madsen, Mads thy, Emil Højgaard, simo
*/

// TODO: Write Documentation

// Input:
// External Identifier : Something the user chooses, to identify the result
    // as an example, Enums, 0 weapon, 1, bedroom object, 2 trash, etc.
    // It will return an external identifier as a result. 
    // up to the programmer how to use it.

// Procentages: a value between 0 and 100

public class Chances
{
    // Intern Objects
    private class Elements
    {
        // Variables
            // Center on a given Length, along a axis
        private double Center;
        
            // Thresholds size
        private double Threshold;
        
            // Position, along x axis
        private double ThresholdPosA;
        private double ThresholdPosB;

            // Given procentage for the given Element
        private double Value;

        // Identifiers
          // External, something the user define
        public int Id_Ext;

          // Used by the class
        private int Id_Internal;
        
        // Initiators
        public Elements( int External_Identifier, 
                         int Internal_Identifier )
        {
            SetExternal( External_Identifier );
            SetInternal( Internal_Identifier );
        }
        
        public Elements( double Procentage,
                         int External_Identifier,
                         int Internal_Identifier )
        {   
            this( External_Identifier, 
                  Internal_Identifier );
            
            SetProcentage( Procentage );
        }
        
        // Tag: Center
            // Finder Centrum af en given Længde
        public final void SetCenter( double iLength )
        {
          Center = iLength/2;
        }

            // Returns Center
        public final double GetCenter()
        {
          return Center;
        }
        
        // Tags: Procentage
        public final double GetProcentage()
        {
            return Value;
        }

        public final void SetProcentage( double Procentage )
        {
            Value = Procentage;
        }
        
        // Tag: Threshold
            // Calculates the threshold
        public void SetThreshold( double iLength )
        {
          double HalfProcentage = Value / 2;

          double Result = ( iLength/100 ) * HalfProcentage;

          Threshold = Result;
        }

            // Returns Threshold
        public double GetThreshold()
        {
          return Threshold;
        }
        
        // Tag: ThresholdPosA
        public void SetThresholdPosA( double Position )
        {
            ThresholdPosA = Position;
        }

        public double GetThresholdPosA()
        {
            return ThresholdPosA;
        }
        
        // Tag: ThresholdPosB
        public void SetThresholdPosB( double Position )
        {
            ThresholdPosB = Position;
        }

        public double GetThresholdPosB()
        {
            return ThresholdPosB;
        }
        
        // Tag: Internal
        public final void SetInternal( int Id )
        {
          Id_Internal = Id;
        }
        
        public final int GetInternal()
        {
          return Id_Internal;
        }
        
        // Tag: External
        public final void SetExternal(int Id)
        {
            Id_Ext = Id;
        }
        
        public final int GetExternal()
        {
            return Id_Ext;
        }

    }

    // Variabler
        // In case of error
    private boolean Error;
    private boolean issueSortElements = false;

      // Maximum length
    private double Maximum;
    private int current_Identifier;

    private boolean Debug = false;

    //
    private ArrayList<Elements> ElementList;

    // -Initialisation Process ------------------------------------------ //
    public final void Initialise()
    {
       Error = false;
       Maximum = 4096.0;

       current_Identifier = 0;

       ElementList = new ArrayList();
    }

    // -Startup Methods ------------------------------------------------- //
    public Chances( double[] NumberOfElements,
                    int[] Identifiers )
    {
        // Retrieves the length of NOE's and Identifiers Array
      int NOE_Length = NumberOfElements.length;
      int Ids_Length = Identifiers.length;

      // If, they not equal, retrieve error
      if( NOE_Length != Ids_Length )
      {
          System.out.print( "Error: 1" );
          // Error
          return;
      }

      // Initialise the class
      Initialise();

      // Get Summerized Size of Elements
      double SummerizedElements = Summerize( NumberOfElements );

      // Checks if the summerized Value, of Elements is above 100 Procent.
      // If it is, throw an error and exit
      if( ( SummerizedElements > 100.0f ) != true )
      {
        // Add Objects to Elements
        AccumulateObjects( NumberOfElements,
                           Identifiers );
      }
      else
      {
          System.out.print( "Error: 2" );
          // Error
          return;
      }

      SortElements();

      ShowArray();
    }

    // -Accumulate Processes -------------------------------------------- //
        // Tilføjer Elementær ud fra Arrays
    private void AccumulateObjects( double[] NumberOfElements,
                                    int[] ext_Id )
    {

        // Tilføjer elementet til listen med dets associeret ID
        for( int x = 0;
                 x <= NumberOfElements.length - 1;
                 x ++ )
        {
            current_Identifier++;

            // Laver et nyt
            Elements element = new Elements( NumberOfElements[x],
                                             ext_Id[x],
                                             current_Identifier );

            ElementList.add( element );
        }

    }

        // Tilføjer Element ud fra et Input
    private void AccumulateObjects( double ElementValue,
                                    int Id )
    {
      current_Identifier++;

      Elements element = new Elements( ElementValue,
                                       Id,
                                       current_Identifier );

      ElementList.add( element );
    }

    // -Add Elements ---------------------------------------------------- //
    public void AddElement( double Procentage,
                            int Extern )
    {
        current_Identifier++;

        Elements Object = new Elements( Procentage,
                                        Extern,
                                        current_Identifier );

        InsertElement( Object );

    }

    public void AddElements( double[] Procentages,
                             int[] Extern )
    {

        if( Procentages.length != Extern.length )
        {
            return;
        }

        for( int x = 0;
                 x <= Procentages.length - 1;
                 x ++ )
        {
            current_Identifier++;

            Elements Object = new Elements( Procentages[x],
                                            Extern[x],
                                            current_Identifier );

            InsertElement( Object );
        }

    }

    // Add Elements to Array
    private void InsertElement( Elements input )
    {
        boolean specificly_insert = false;

        int End = ElementList.size() - 1;
        int Destination = 0;

        for( int x = 0;
                // Inside Search range
                 x <= ( End - 1 );
                 x ++ )
        {
            // If, it's already the biggest, just skip
            if( x == 0 )
            {
                Elements Element0 = ElementList.get( x );

                if( Element0.Value < input.Value )
                {
                    specificly_insert = true;
                    Destination = x;

                    break;
                }
            }

            Destination = x;

            int Forward_Index = x + 1;

            // Add buffers
            Elements current = ElementList.get( x );
            Elements forward = ElementList.get( Forward_Index );

            //BT = Bigger Than
            //ST = Smaller Than
            boolean BT_current = false,
                    ST_forward = false;

            // if element Input is smaller than current Value
            if( current.Value > input.Value )
                BT_current = true;

            // if element S is Bigger than Forward
            if( forward.Value < input.Value )
                ST_forward = true;

            // If it fits inbetween two values
            if( ( BT_current && ST_forward ) == true )
            {
                Destination++;
                specificly_insert = true;

                break;
            }

        }

        if( specificly_insert == true )
        {
            // Places in between two values, specificly
            ElementList.add( Destination,
                             input );
        }
        else
        {
            // places in the bottom
            ElementList.add( input );
        }

    }

    public void RemoveElement( int Extern )
    {
        // Indexes, to be remove
        ArrayList<Integer> Indexes = new ArrayList();

        // Find x, extern values that matches
        for( int x = 0;
                 x <= ElementList.size() - 1;
                 x ++ )
        {
            Elements current = ElementList.get( x );

            if( current.Id_Ext == Extern )
            {
                Indexes.add( x );
            }

        }

        // Space thats been removed
        int removed_space = 0;

        // remove them
        for( int x = 0;
                 x <= Indexes.size() - 1;
                 x ++ )
        {
            int current = Indexes.get(x);

            ElementList.remove( ( current - removed_space ) );
            removed_space++;
        }

    }

    public void RemoveElements( int[] Extern )
    {
        // Indexes, to be remove
        ArrayList<Integer> Indexes = new ArrayList();

        // Searches list for elements
        for( int x = 0;
                 x <= ElementList.size() - 1;
                 x ++ )
        {
            Elements current = ElementList.get( x );

            for( int i = 0;
                     i <= Extern.length - 1;
                     i ++ )
            {
                if( Extern[i] == current.Id_Ext )
                {
                    Indexes.add( x );
                }
            }

        }

        int removed_space = 0;

        for( int x = 0;
                 x <= Indexes.size() - 1;
                 x ++ )
        {
            int current = Indexes.get(x);

            ElementList.remove( current - removed_space );
            removed_space++;
        }

    }

    private double posA,
                   posB;

    // -Functions ------------------------------------------------------- //
    public int Calculate()
    {
        if( issueSortElements == true )
            SortElements();

        Initialise_Elements();

        int i = Retrieve_R_Element();

        return i;
    }

    private void Initialise_Elements()
    {
        posA = Maximum/2;
        posB = Maximum/2;

        for( int x = 0;
                 x <= ElementList.size() - 1;
                 x ++ )
        {
            ChangeElement(x);
        }

    }

    private int Retrieve_R_Element()
    {
        // Får et random tal, kan evt. ved hjælp af en random, hash gøres mere tilfældig
        java.util.Random r_generator = new Random();

        // x aksens længde
        double frg = Maximum;

        // retunere et sted mellem 0 og Maximum
        frg = ( frg * r_generator.nextGaussian() );

        // den tætteste index
        int indexA = 0;

        for( int x = 0;
                 x <= ElementList.size() - 1;
                 x ++ )
        {
            // get last Element
            Elements Last = ElementList.get( indexA );
            Elements Current = ElementList.get( x );

            double cDistance = RetrieveLowestDistance( frg,
                                                       Current );

            double lDistance = RetrieveLowestDistance( frg,
                                                       Last );

            if( cDistance > lDistance )
                indexA = x;
        }

        Elements RandomObject = ElementList.get( indexA );

        // Retunere extern Id
        return RandomObject.Id_Ext;
    }

    private double RetrieveLowestDistance( double Position,
                                           Elements CurrentObject )
    {
        double lengthA = CalculateDistance( Position,
                                            CurrentObject.GetThresholdPosA() );

        double lengthB = CalculateDistance( Position,
                                            CurrentObject.GetThresholdPosB() );

        // Return
        if( ( lengthA < lengthB ) )
            return lengthA;
        else
            return lengthB;
    }

    private double CalculateDistance( double A,
                                      double B )
    {
        if( A >= B )
            return( A - B );
        else
            return( B - A );
    }

    private void ChangeElement( int Element )
    {
        // Retrieve Element
        Elements ChosenElement = ElementList.get( Element );

        // Sætter Centrum på x Aksen
        ChosenElement.SetCenter( Maximum );

        // Bestemmer dets thresshold ud fra
        ChosenElement.SetThreshold( Maximum );

        // Position Calculated
        posA = posA - ChosenElement.GetThreshold();
        posB = posB - ChosenElement.GetThreshold();

        // Insert Position
        ChosenElement.SetThresholdPosA( posA );
        ChosenElement.SetThresholdPosB( posB );

        // Inserts Element back
        ElementList.set( Element,
                         ChosenElement );
    }

    // -Sorting ---------------------------------------------------------- //
      // Sortere Elementer ud fra mindst -> Størst, ud fra procent sats
    private void SortElements()
    {

      for( int x = ElementList.size() - 1;
               x >= 0;
               x -- )
      {

        int y = getHighestElement( 0,
                                   x );

        if( y == -1 )
        {
          break;
        }

        SwitchPosition( x,
                        y );

      }

    }

    // Finder det højeste element i en array,
    // inde for et specific område
    private int getHighestElement( int start,
                                   int end )
    {
      // given that a mistake is made
      if( ( end - start ) < 0 )
        return -1;

      int index = 0;

      for( int x = start;
               x <= end;
               x ++ )
      {
        // Current Element in Array
        Elements currentIndex = ElementList.get( x );

        // Get Last
        Elements currentHighestElement = ElementList.get( index );

        // Hvis det er det, samme skip
        if( currentIndex.Value == currentHighestElement.Value )
          continue;

        if( currentIndex.Value <= currentHighestElement.Value )
        {
          index = x;
        }

      }

      return index;
    }

    // Switch 2 positions in an array
    private void SwitchPosition( int p1, int p2 )
    {
      Elements tempBuffer;

      // Temperary Buffer, loader ind, et element
      tempBuffer = ElementList.get( p2 );

      // Moves the Objects
        // Replaces, the tempbuffer position, in the array
      ElementList.set( p2,
                       ElementList.get( p1 ) );
      
        // Replaces first element, with the tempbuffer
      ElementList.set( p1,
                       tempBuffer );

    }

    // -Summerizing ------------------------------------------------------ //
    // Takes an array as input and adds them together
    private double Summerize( double[] ArrayInput )
    {
        double ReturnValue = 0;

        for( int x = 0;
                 x <= ArrayInput.length - 1;
                 x ++ )
        {
            ReturnValue = ReturnValue + ArrayInput[x];
        }

        return ReturnValue;
    }

    // -Debugging ---------------------------------------------------------- //
    private void ShowArray()
    {
        if( Debug == false )
            return;

        for( int x = 0;
                 x <= ElementList.size() - 1;
                 x ++ )
      {
          Elements a = ElementList.get(x);

          System.out.println( "Procentage:" + a.Value +
                              ", Id: " + a.Id_Internal +
                              ", Pos in A:" + x );
      }

    }

    private void ShowResults()
    {
        if( Debug == false )
            return;

        for( int x = 0;
                 x <= ElementList.size() - 1;
                 x ++ )
        {
            Elements a = ElementList.get(x);

            System.out.println();
        }
    }

}

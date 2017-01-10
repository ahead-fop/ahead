layer Benchmark;

import  java.io.*;


   public
refines class Graph {
    
    public Reader inFile;               // File handler for reading
    public int ch;               // Character to read/write
	
    // timmings
    long last=0, current=0, accum=0;
      
    public void runBenchmark(String FileName) throws IOException
    {
	try {
		inFile = new FileReader(FileName);
	} catch (IOException e)
	{
		System.out.println("Your file " + FileName + " cannot be read");
	}		  
    }
	  
    public void stopBenchmark() throws IOException
    {
	inFile.close();
    }
	  
    public int readNumber() throws IOException
    {
  	int index =0;
	char[] word = new char[80];
	int ch=0;
		
	ch = inFile.read();
	while(ch==32) ch = inFile.read();  // skips extra whitespaces
		
	while(ch!=-1 && ch!=32 && ch!=10) // while it is not EOF, WS, NL
	{
	  word[index++] = (char)ch;
	  ch = inFile.read();
	}
	word[index]=0;
		
	String theString = new String(word);
		
	theString = new String(theString.substring(0,index));
	return Integer.parseInt(theString,10);
     }
	  
     public void startProfile()
     {
       accum = 0;
       current = System.currentTimeMillis();
       last = current;
     }
	  
     public void stopProfile()
     {
       current = System.currentTimeMillis();
       accum = accum + (current - last);
     }
	  
     public void resumeProfile()
     {
      current = System.currentTimeMillis();
      last = current;
     }
	  
     public void endProfile()
     {
	 current = System.currentTimeMillis();
	 accum = accum + (current-last);
	 System.out.println("Time elapsed: " + accum + " milliseconds");
     }
      
   }
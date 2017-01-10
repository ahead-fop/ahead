// Class that creates the Jedi Manifest file
// Date: March 5, 2002
import java.io.File;
import java.net.MalformedURLException;

public class JediManifestMaker {
    public static void main(String[] args) {
	System.out.println("Manifest-Version: 1.0");
	System.out.println("Main-Class: jedi.Start");
	System.out.print("Class-Path:");
	System.out.print(" file:jakarta.jar");
	System.out.print(" file:velocity.jar");
	System.exit(0);
    }
}

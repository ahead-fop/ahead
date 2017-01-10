public class inventor {
   private static final String quote = "\"";
   private static final String comma = ",";

   protected String category;
   protected String invention;
   protected String inventor;
   protected int    year;
   protected int    nation_code;

   public inventor() { }
   public inventor( inventor x ) { 
      category    = x.category;
      invention   = x.invention;
      inventor    = x.inventor;
      year        = x.year;
      nation_code = x.nation_code;
   }
   public inventor ( String c, String i, String n, int y, int a ) {
      category    = c;
      invention   = i;
      inventor    = n;
      year        = y;
      nation_code = a;
   }

   public void print() {
      System.out.println( quote + category + quote + comma +
                          quote + invention + quote + comma +
                          quote + inventor + quote + comma +
                          year + comma + nation_code );
   }

   public String category() { return category; }
   public String category( String x ) { return category = x; }

   public String invention() { return invention; }
   public String invention( String x ) { return invention = x; }

   public String inventor() { return inventor; }
   public String inventor( String x ) { return inventor = x; }

   public int year() { return year; }
   public int year( int x ) { return year = x; }

   public int nation_code() { return nation_code; }
   public int nation_code( int x ) { return nation_code = x; }

   public static inventor[] rawdata = {
        new inventor("Clock","Chronometer","Harrison",1735,156),
        new inventor("Clock","Pendulum","Huygens",1657,106),
        new inventor("Communication","Movable Type","Gutenberg",1450,56),
        new inventor("Communication","Addressograph","Duncan",1892,157),
        new inventor("Communication","Braille Printing","Braille",1829,52),
        new inventor("Communication","Condenser Microphone","Wente",1920,157),
        new inventor("Communication","Magnetic Telegraph","Morse",1837,157),
        new inventor("Communication","Microphone","Berliner",1877,157),
        new inventor("Communication","Telephone","Bell",1876,157),
        new inventor("Communication","Teletype","Morkrum, Kleinschmidt",1928,157),
        new inventor("Communication","Television","Farnsworth",1927,157),
        new inventor("Communication","Typewriter","Soule, Glidden",1868,157),
        new inventor("Communication","Wireless Telegraph","Marconi",1837,74),
        new inventor("Computer","Adding Machine","Pascal",1642,52),
        new inventor("Computer","Automatic Sequence","Aiken et al",1939,157),
        new inventor("Computer","Calculating Machine","Babbage",1823,156),
        new inventor("Computer","Punch Card Accounting","Hollerith",1884,157),
        new inventor("Electronics","Circuit Breaker","Hiliard",1925,157),
        new inventor("Electronics","Transistor","Shockley, Brattain, Bardeen",1947,157),
        new inventor("Engine","Automobile","Benz",1879,56),
        new inventor("Engine","Coal-Gas 4-Cycle","Otto",1877,56),
        new inventor("Engine","Diesel","Diesel",1895,56),
        new inventor("Engine","Electric Ignition","Benz",1880,56),
        new inventor("Engine","Gas Turbine","Curtis, C.G.",1899,157),
        new inventor("Engine","Gasoline","Daimler",1886,56),
        new inventor("Engine","Hydraulic Turbine","Francis",1849,157),
        new inventor("Engine","Rocket","Goddard",1929,157),
        new inventor("Engine","Steam Turbine","Parsons",1884,156),
        new inventor("Engine","Steam, Piston","Newcomen",1705,156),
        new inventor("Engine","Steam, Practical","Watt",1769,156),
        new inventor("Farm Equipment","Cast Iron Plow","Newbold",1797,157),
        new inventor("Farm Equipment","Cotton Gin","Whitney",1793,157),
        new inventor("Farm Equipment","Cream Separator","DeLaval",1880,142),
        new inventor("Farm Equipment","Disc Cultivator","Mallon",1878,157),
        new inventor("Farm Equipment","Disc Plow","Hardy",1896,157),
        new inventor("Farm Equipment","Harvester-Thresher","Matteson",1888,157),
        new inventor("Farm Equipment","Lawn Mower","Hills",1868,157),
        new inventor("Farm Equipment","Reaper","McCormick",1834,157),
        new inventor("Farm Equipment","Spinning Jenny","Hargreaves",1767,156),
        new inventor("Lamp","Arc","Brush",1879,157),
        new inventor("Lamp","Incandescent","Edison",1879,157),
        new inventor("Lamp","Mercury Vapor","Hewitt",1912,157),
        new inventor("Lamp","Neon","Claude",1915,52),
        new inventor("Machine","Addressograph","Duncan",1892,157),
        new inventor("Machine","Air Brake","Westinghouse",1868,157),
        new inventor("Machine","Air Conditioning","Carrier",1911,157),
        new inventor("Machine","Air Pump","Guericke",1650,56),
        new inventor("Machine","Bottling","Owens",1903,157),
        new inventor("Machine","Cash Register","Ritty",1879,157),
        new inventor("Machine","Electric Vacuum Cleaner","Spangler",1907,157),
        new inventor("Machine","Electric Washer","Hurley Co.",1907,157),
        new inventor("Machine","Electrostatic Generator","Van De Graaff",1929,157),
        new inventor("Machine","Elevator Brake","Otis",1852,157),
        new inventor("Machine","Ice-Making","Gorrie",1851,157),
        new inventor("Machine","Power Loom","Cartwright",1785,156),
        new inventor("Machine","Screw Propeller","Stevens",1804,157),
        new inventor("Machine","Sewing Machine","Howe",1846,157),
        new inventor("Machine","Shrapnel Shell","Shrapnel",1784,156),
        new inventor("Machine","Stock Ticker","Edison",1870,157),
        new inventor("Machine","Turrent Lathe","Fitch",1845,157),
        new inventor("Mathematics","Calculus","Newton",1670,156),
        new inventor("Mathematics","Slide Rule","Oughtred",1620,156),
        new inventor("Medicine","Antiseptic Surgery","Lister",1867,156),
        new inventor("Medicine","Aspirin","Dresser",1889,56),
        new inventor("Medicine","Cortisone","Kendall",1936,157),
        new inventor("Medicine","Electroencephalograph","Berger",1929,56),
        new inventor("Medicine","Stethoscope","Laennec",1819,157),
        new inventor("Misc","Aerosol Spray","Goodhue",1941,157),
        new inventor("Misc","Ballpoint Pen","Loud",1888,157),
        new inventor("Misc","Barbed Wire","Glidden",1874,157),
        new inventor("Misc","Bifocal Glasses","Franklin",1780,157),
        new inventor("Misc","Canning Food","Appert",1804,52),
        new inventor("Misc","Cellophane","Brandenberger",1911,143),
        new inventor("Misc","Cylinder Lock","Yale",1865,157),
        new inventor("Misc","DDT","Zeidler",1874,56),
        new inventor("Misc","Dynamite","Nobel",1866,142),
        new inventor("Misc","Electric Razor","Schick",1931,157),
        new inventor("Misc","Electric Stove","Hadaway",1896,157),
        new inventor("Misc","Electric Welding","Thomson",1877,157),
        new inventor("Misc","Fountain Pen","Waterman",1884,157),
        new inventor("Misc","Friction Match","John Walker",1827,156),
        new inventor("Misc","Kaleidoscope","Brewster",1817,156),
        new inventor("Misc","Laminated Safety Glass","Benedictus",1909,52),
        new inventor("Misc","Machine Gun","Gatling",1861,157),
        new inventor("Misc","Magnetic Tape Recorder","Poulsen",1899,41),
        new inventor("Misc","Mason Jar","Mason, J.",1858,157),
        new inventor("Misc","Metronome","Malzel",1816,9),
        new inventor("Misc","Micrometer","Gascoigne",1636,156),
        new inventor("Misc","Neoprene","Carothers",1930,157),
        new inventor("Misc","Revolver","Colt",1835,157),
        new inventor("Misc","Safety Pin","Hunt",1849,157),
        new inventor("Misc","Saftey Razor","Gillette",1895,157),
        new inventor("Misc","Stainless Steel","Brearley",1916,156),
        new inventor("Misc","Steel Manufacturing","Bessemer",1856,156),
        new inventor("Misc","Tire, Pneumatic","Dunlop",1888,156),
        new inventor("Misc","Toaster, Automatic","Strite",1918,157),
        new inventor("Misc","Wind Tunnel","Munk",1923,157),
        new inventor("Misc","Zipper","Judson",1891,157),
        new inventor("Petroleum","Gas Burner","Bunsen",1855,56),
        new inventor("Petroleum","Gas Lighting","Murdoch",1792,156),
        new inventor("Petroleum","Gasoline Carborator","Daimler",1876,56),
        new inventor("Petroleum","High Octane Gasoline","Ipatieff",1930,154),
        new inventor("Photoelectric Cell","","Elster",1895,56),
        new inventor("Photography","Color","Ives",1892,157),
        new inventor("Photography","Concept","Talbot",1835,156),
        new inventor("Photography","Kodak Camera","Eastman, Walker",1888,157),
        new inventor("Photography","Photographic Paper","Baekeland",1898,157),
        new inventor("Photography","Polaroid Land Camera","Land",1948,157),
        new inventor("Photography","Talking Movies","Warner Bros",1927,157),
        new inventor("Photography","Transparent Film","Eastman, Goodwin",1878,157),
        new inventor("Piano","Original","Cristofori",1709,74),
        new inventor("Piano","Player","Fourneaux",1863,52),
        new inventor("Radio","Electric Waves","Hertz",1888,56),
        new inventor("Radio","Radar","Taylor, Young",1922,157),
        new inventor("Radio","Signals","Marconi",1895,74),
        new inventor("Radio","Tube-Diode","Fleming",1905,156),
        new inventor("Record","Cylinder","Bell, Tainter",1887,157),
        new inventor("Record","Disc","Berliner",1887,157),
        new inventor("Record","Long Playing","Goldmark",1948,157),
        new inventor("Record","Phonograph","Edison",1877,157),
        new inventor("Record","Wax Cylinder","Edison",1888,157),
        new inventor("Rubber","Vulcanized","Goodyear",1839,157),
        new inventor("Science","Astonomical Telescope","Kepler",1611,56),
        new inventor("Science","Atomic Theory","Dalton",1803,156),
        new inventor("Science","Bacteria First Described","Leeuwenhoek",1676,106),
        new inventor("Science","Barometer","Torricelli",1643,74),
        new inventor("Science","Blood Circulation","Harvey",1628,156),
        new inventor("Science","Cathode Ray Tube","Crookes",1878,156),
        new inventor("Science","Combustion First Explained","Lavoisier",1777,52),
        new inventor("Science","Compound Microscope","Janssen",1590,106),
        new inventor("Science","Conditioned Reflex","Pavlov",1914,154),
        new inventor("Science","Cosmic Rays","Gockel",1910,143),
        new inventor("Science","Cyclotron","Lawrence",1930,157),
        new inventor("Science","DNA Structure","Crick, Watson",1951,156),
        new inventor("Science","Electric Battery","Volta",1800,74),
        new inventor("Science","Electric Resistance Law","Ohm",1827,56),
        new inventor("Science","Electrolysis","Faraday",1852,156),
        new inventor("Science","Electromagnet","Sturgeon",1824,156),
        new inventor("Science","Electromagnetism","Oersted",1819,41),
        new inventor("Science","Electron","Thompson, J.",1897,156),
        new inventor("Science","Electroplating","Brugnatelli",1805,74),
        new inventor("Science","Geiger Counter","Geiger",1913,56),
        new inventor("Science","Gyroscope","Foucault",1852,52),
        new inventor("Science","Lightning Rod","Franklin",1752,157),
        new inventor("Science","Mercury Thermometer","Fahrenheit",1714,56),
        new inventor("Science","Pendulum","Galileo",1581,74),
        new inventor("Science","Telescope","Galileo",1609,74),
        new inventor("Science","Thermometer","Galileo",1593,74),
        new inventor("Travel (Air)","Balloon","Montgolfier",1783,52),
        new inventor("Travel (Air)","Glider","Cayley",1853,156),
        new inventor("Travel (Air)","Helicopter","Sikorsky",1939,157),
        new inventor("Travel (Air)","Jet","Ohain",1939,56),
        new inventor("Travel (Air)","Motorized Airplane","Wright Bros.",1903,157),
        new inventor("Travel (Air)","Parachute","Blanchard",1785,52),
        new inventor("Travel (Air)","Rigid Dirigible","Zeppelin",1900,56),
        new inventor("Travel (Land)","Bicycle","Starley",1884,156),
        new inventor("Travel (Land)","Electric Powered Automobile","Morrison",1892,157),
        new inventor("Travel (Land)","Gasoline Powered Automobile","Daimler",1887,56),
        new inventor("Travel (Land)","Military Tank","Swinton",1914,156),
        new inventor("Travel (Land)","Motorcycle","Daimler",1885,56),
        new inventor("Travel (Land)","Practical Locomotive","Stephenson",1829,156),
        new inventor("Travel (Land)","Sleeping-Car","Pullman",1858,157),
        new inventor("Travel (Land)","Steam Powered Automobile","Roper",1889,157),
        new inventor("Travel (Water)","Practical Steamboat","Symington",1802,156),
        new inventor("Travel (Water)","Submarine","Holland",1891,157)
   };
}

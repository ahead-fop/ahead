/* GPL domain model March 2006 */

// grammar of feature model

GPL : Prog Benchmark Alg+ [Src] HiddenWgt Wgt HiddenGtp
      Gtp Implementation Base :: MainGpl ;

Implementation : AL | NL | EL ;

Gtp : Directed | Undirected ;

HiddenGtp : DirectedEL | DirectedNL | DirectedAL
          | UndirectedEL  | UndirectedNL | UndirectedAL ;

Wgt : Weighted | Unweighted ;

HiddenWgt : [WeightedEL] [WeightedNL] [WeightedAL] :: WeightOptions ;

Src : BFS | DFS ;

Alg : Number | Connected |  StronglyConnected Transpose :: StrongC
    | Cycle |  MSTPrim [PrimOpts] ::MstPrim 
    | MSTKruskal [KruskOpts] :: MstKruskal;

PrimOpts : MSTPrimAL | MSTPrimNL ;

KruskOpts : MSTKruskalAL | MSTKruskalNL ;


%% // domain constraints

Number implies Gtp and Src ;
Connected implies Undirected and Src;
StrongC implies Directed and DFS ;
Cycle implies Gtp and DFS;
MSTKruskal or MSTPrim implies Undirected and Weighted ;
MSTKruskal or MSTPrim implies not (MSTKruskal and MSTPrim) ;

// implementation constraints

AL and Weighted implies WeightedAL;
NL and Weighted implies WeightedNL;
EL and Weighted implies WeightedEL;
AL and Directed implies DirectedAL;
NL and Directed implies DirectedNL;
EL and Directed implies DirectedEL;
AL and Undirected implies UndirectedAL;
NL and Undirected implies UndirectedNL;
EL and Undirected implies UndirectedEL;

AL and MSTPrim implies MSTPrimAL;
NL and MSTPrim implies MSTPrimNL;
AL and MSTKruskal implies MSTKruskalAL;
NL and MSTKruskal implies MSTKruskalNL;

## // formatting

HiddenGtp { hidden }
HiddenWgt { hidden }
PrimOpts  { hidden }
KruskOpts { hidden }

AL { disp="OnlyVertices   (AL)" }
NL { disp="WithNeighbors  (NL)" }
EL { disp="WithEdges      (EL)" }

Directed {   out="" }
Undirected { out="" }
Weighted {   out="" }
Unweighted { out="" }

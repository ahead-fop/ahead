// this feature model is made available from the University of
// Texas at Austin by the Product Line Research Architecture
// research group at http://www.cs.utexas.edu/users/schwartz/


SvgMapApp : [L2Build] Layers Legends USStates Base :: appdef ;

Legends : [Controls] [Stats1] [Stats2] [Legend] :: ShowLegend;

Stats1 : [AgeChart] [StatsMedianAge] [EthnicBarChart] [EthnicPieChart] ::s1;
Stats2 : [StatsSex] [StatsHouseholds] [StatsPopulation] :: s2;
Controls : [Navigator] [ReliefControls] [RiverControls] [LakeControls] [PopCircleControls] [CoordinateDisplay] ::c1 ;
Layers : [ColorRegion] [Relief] [Rivers] [Lakes] [PopCircle] :: layer1 ;

%%

PopCircleControls implies PopCircle;
ReliefControls implies Relief;
RiverControls implies Rivers;
LakeControls implies Lakes;
Controls implies Legend;
Stats1 implies Legend;
Stats2 implies Legend;
L2Build implies not EthnicBarChart;

##
Legend { hidden }   // this turns panel a off
USStates { hidden }   // this turns panel a off
Layers { tab }
Legends { out="" }
Controls { tab }
Stats1 { tab }
Stats2 { tab }






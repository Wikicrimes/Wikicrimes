package org.wikicrimes.util.kernelMap;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author mairon
 *
 */
public class Dados {
	List <Point> lst = new ArrayList<Point>();
	List <Ponto> lstDouble = new ArrayList<Ponto>();
	
	public void limpaDados (){
		lstDouble = new ArrayList<Ponto>();
	}
	
	public List<Point> getPontosSimpol(){		
		return lst;
	}
	
	public void setPontosSimpol(List<Point> lst) {
		this.lst = lst;
	}
	
//	Ponto clicado: -122.18770980834961 / 37.43520348658563
//	Ponto clicado: -122.14822769165039 / 37.441882193395124
//	Ponto clicado: -122.1731185913086 / 37.456736770757715
//	Ponto clicado: -122.12797164916992 / 37.43029630177328
//	Ponto clicado: -122.15629577636719 / 37.45482900849452
//	Ponto clicado: -122.16058731079102 / 37.42088996502089

	
	public void adicionaPontoWiki(double logitude, double latitude){
		lstDouble.add(new Ponto(logitude, latitude));
	}

	
	public List<Ponto> getPontosWiki(){
		return lstDouble;
	}
	
	public List<Ponto> getPontosWikiFixo(){
		lstDouble = new ArrayList<Ponto>();
		
		lstDouble.add(new Ponto(-122.18770980834961 , 37.43520348658563));
		lstDouble.add(new Ponto(-122.14822769165039 , 37.441882193395124));
		lstDouble.add(new Ponto(-122.1731185913086 , 37.456736770757715));
		lstDouble.add(new Ponto(-122.12797164916992 , 37.43029630177328));
		lstDouble.add(new Ponto(-122.15629577636719 , 37.45482900849452));
		lstDouble.add(new Ponto(-122.16058731079102 , 37.42088996502089));
		
		return lstDouble;
	}
	
	public List<Point> getPontosReais(){
		 
		lst.add(new Point(55740005, 958828509));
		lst.add(new Point(55675778, 958807435));
		lst.add(new Point(55706536, 958819684));
		lst.add(new Point(55725324, 958824269));
		lst.add(new Point(55728904, 958825141));
		lst.add(new Point(55767372, 958801313));
		lst.add(new Point(55730259, 958825452));
		lst.add(new Point(55446449, 958838518));
		lst.add(new Point(55629639, 958807289));
		lst.add(new Point(55415154, 958808889));
		lst.add(new Point(55599045, 958819823));
		lst.add(new Point(55595543, 958770711));
		lst.add(new Point(55601126, 958787756));
		lst.add(new Point(55492706, 958835611));
		lst.add(new Point(55629639, 958807289));
		lst.add(new Point(55680131, 958750910));
		lst.add(new Point(55422136, 958819679));
		lst.add(new Point(55494909, 958802565));
		lst.add(new Point(55576443, 958823926));
		lst.add(new Point(55422540, 958806718));
		lst.add(new Point(55679821, 958756910));
		lst.add(new Point(55511070, 958811116));
		lst.add(new Point(55550136, 958821614));
		lst.add(new Point(55674924, 958757110));
		lst.add(new Point(55670693, 958746510));
		lst.add(new Point(55645622, 958766666));
		lst.add(new Point(55412362, 958812365));
		lst.add(new Point(55676510, 958708810));
		lst.add(new Point(55658419, 958750465));
		lst.add(new Point(55550136, 958821614));
		lst.add(new Point(55660155, 958749911));
		lst.add(new Point(55448414, 958820258));
		lst.add(new Point(55409127, 958793585));
		lst.add(new Point(55679821, 958756910));
		lst.add(new Point(55588068, 958822211));
		lst.add(new Point(55640162, 958724142));
		lst.add(new Point(55471492, 958830335));
		lst.add(new Point(55602031, 958680096));
		lst.add(new Point(55637882, 958721082));
		lst.add(new Point(55550451, 958664710));
		lst.add(new Point(55617342, 958727711));
		lst.add(new Point(55408979, 958776406));
		lst.add(new Point(55581741, 958727072));
		lst.add(new Point(55639061, 958720711));
		lst.add(new Point(55408367, 958776611));
		lst.add(new Point(55456279, 958723343));
		lst.add(new Point(55449086, 958780511));
		lst.add(new Point(55590175, 958716974));
		lst.add(new Point(55415242, 958756912));
		lst.add(new Point(55443823, 958765011));
		lst.add(new Point(55598579, 958733855));
		lst.add(new Point(55600968, 958676905));
		lst.add(new Point(55497504, 958730112));
		lst.add(new Point(55456279, 958723343));
		lst.add(new Point(55642815, 958622711));
		lst.add(new Point(55537885, 958704811));
		lst.add(new Point(55394496, 958732792));
		lst.add(new Point(55455843, 958743666));
		lst.add(new Point(55516800, 958675840));
		lst.add(new Point(55642739, 958683193));
		lst.add(new Point(55531054, 958683524));
		lst.add(new Point(55541474, 958752111));
		lst.add(new Point(55426248, 958687385));
		lst.add(new Point(55638825, 958720783));
		lst.add(new Point(55424037, 958665653));
		lst.add(new Point(55500958, 958704893));
		lst.add(new Point(55573868, 958705474));
		lst.add(new Point(55625009, 958640611));
		lst.add(new Point(55624437, 958638871));
		lst.add(new Point(55638825, 958720783));
		lst.add(new Point(55646422, 958633611));
		lst.add(new Point(55514129, 958667592));
		lst.add(new Point(55431349, 958727222));
		lst.add(new Point(55488668, 958703256));
		lst.add(new Point(55746269, 958747460));
	
		return lst;
	}
	
	public List<Point> getPontos(){
		lst.add(new Point(55675053, 958648611));
		lst.add(new Point(55604102, 958723796));
		lst.add(new Point(55586690, 958613563));
		lst.add(new Point(55516848, 958749911));
		lst.add(new Point(55525564, 958703424));
		lst.add(new Point(55519454, 958758011));
		lst.add(new Point(55348322, 958714812));
		lst.add(new Point(55650533, 958680618));
		lst.add(new Point(55575101, 958708333));
		lst.add(new Point(55607253, 958622211));
		lst.add(new Point(55585059, 958629411));
		lst.add(new Point(55547415, 958660064));
		lst.add(new Point(55509317, 958726211));
		lst.add(new Point(55654955, 958607524));
		lst.add(new Point(55412749, 958775162));
		lst.add(new Point(55407648, 958733138));
		lst.add(new Point(55402356, 958841411));
		lst.add(new Point(55639061, 958720711));
		lst.add(new Point(55489066, 958663812));
		lst.add(new Point(55574042, 958705311));
		lst.add(new Point(55493806, 958757547));
		lst.add(new Point(55591692, 958648436));
		lst.add(new Point(55422917, 958782372));
		lst.add(new Point(55669559, 958631524));
		lst.add(new Point(55376292, 958699534));
		lst.add(new Point(55590458, 958663986));
		lst.add(new Point(55419459, 958690311));
		lst.add(new Point(55615409, 958647917));
		lst.add(new Point(55559221, 958709971));
		lst.add(new Point(55550502, 958652352));
		lst.add(new Point(55659233, 958636368));
		lst.add(new Point(55525242, 958738511));
		lst.add(new Point(55577130, 958788511));
		lst.add(new Point(55498261, 958655176));
		lst.add(new Point(55466466, 958715812));
		lst.add(new Point(55527298, 958745032));
		lst.add(new Point(55523691, 958697747));
		lst.add(new Point(55525242, 958738511));
		lst.add(new Point(55592323, 958723611));
		lst.add(new Point(55585485, 958665611));
		lst.add(new Point(55466703, 958673912));
		lst.add(new Point(55577811, 958716411));
		lst.add(new Point(55401953, 958714611));
		lst.add(new Point(55621353, 958665811));
		lst.add(new Point(55402003, 958697292));
		lst.add(new Point(55540056, 958674387));
		lst.add(new Point(55588877, 958676363));
		lst.add(new Point(55674752, 958647620));
		lst.add(new Point(55483537, 958726884));
		lst.add(new Point(55519454, 958758011));
		lst.add(new Point(55595898, 958698311));
		lst.add(new Point(55560128, 958661811));
		lst.add(new Point(55430105, 958804111));
		lst.add(new Point(55650847, 958692819));
		lst.add(new Point(55651596, 958692564));
		lst.add(new Point(55432637, 958770535));
		lst.add(new Point(55588315, 958737289));
		lst.add(new Point(55567197, 958683411));
		lst.add(new Point(55526821, 958743509));
		lst.add(new Point(55526761, 958743319));
		lst.add(new Point(55554775, 958830911));
		lst.add(new Point(55925666, 958592210));
		lst.add(new Point(55419542, 958730564));
		lst.add(new Point(55521613, 958748423));
		lst.add(new Point(55667815, 958626611));
		lst.add(new Point(55527154, 958708311));
		lst.add(new Point(55409521, 958758752));
		lst.add(new Point(55595917, 958734711));
		lst.add(new Point(55393628, 958697412));
		lst.add(new Point(55604517, 958702704));
		lst.add(new Point(55496392, 958765212));
		lst.add(new Point(55595917, 958734711));
		lst.add(new Point(55407584, 958692911));
		lst.add(new Point(55452341, 958711127));
		lst.add(new Point(55653136, 958728411));
		lst.add(new Point(55663624, 958760711));
		lst.add(new Point(55616375, 958690527));
		lst.add(new Point(55675407, 958672895));
		lst.add(new Point(55596184, 958662111));
		lst.add(new Point(55606474, 958731311));
		lst.add(new Point(55594671, 958583111));
		lst.add(new Point(55610528, 958669311));
		lst.add(new Point(55646422, 958633611));
		lst.add(new Point(55581256, 958630680));
		lst.add(new Point(55533673, 958718180));
		lst.add(new Point(55538037, 958668097));
		lst.add(new Point(55599331, 958697180));
		lst.add(new Point(55638307, 958600434));
		lst.add(new Point(55523691, 958697747));
		lst.add(new Point(55390647, 958720896));
		lst.add(new Point(55471312, 958755445));
		lst.add(new Point(55510407, 958641689));
		lst.add(new Point(55434181, 958775266));
		lst.add(new Point(55621984, 958605751));
		lst.add(new Point(55613277, 958641415));
		lst.add(new Point(55485898, 958733911));
		lst.add(new Point(55585777, 958701516));
		lst.add(new Point(55409506, 958671064));
		lst.add(new Point(55636787, 958713971));
		lst.add(new Point(55423147, 958783480));
		
		return lst;
	}
}

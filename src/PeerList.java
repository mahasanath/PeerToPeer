import java.util.ArrayList;
/**
 * 
 * @author maha sanath
 * A POJO class which is used to store the portNo and hostName
 * 
 */

public class PeerList {
private int portNo;
private String hostName;
private ArrayList<Integer> rfcList;
private String title;

PeerList(){
	rfcList=new ArrayList<Integer>();
}
public int getPortNo() {
	return portNo;
}
public void setPortNo(int portNo) {
	this.portNo = portNo;
}
public String getHostName() {
	return hostName;
}
public void setHostName(String hostName) {
	this.hostName = hostName;
}
public ArrayList<Integer> getRfcList(){
	return rfcList;
}

public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}

}

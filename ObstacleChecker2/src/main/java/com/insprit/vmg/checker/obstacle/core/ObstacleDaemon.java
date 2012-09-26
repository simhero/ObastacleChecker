package com.insprit.vmg.checker.obstacle.core;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.insprit.vmg.checker.obstacle.core.Config;
import com.nate.uaprof.ucinfo.CustProfileInfo;
import com.nate.uaprof.ucinfo.SearchChgPlusProfile;

/**
 * Obstacle Daemon Server (ObstacleDaemon)
 *
 * @author �ɺ�ö
 * @author <a href="mailto:simhero@in-sprit.com">�ɺ�ö</a>
 * @author <a href="mailto:simhero@gmail.com">�ɺ�ö</a>
 * @version $Id: ObstacleDaemon.java,v 1.0 2009/05/07 00:00:00 �ɺ�ö Express $
 */
public class ObstacleDaemon {
	protected final Log logger = LogFactory.getLog(getClass());
	private Connection connection;
	private Statement statement;
	private ResultSet rs;
	private static Config config = null;

	public static void main(String[] args) {
		try {
			config = Config.instance("xml/ObstacleConfig.xml");
			String checkOrder = args[0];
			if (checkOrder == null) {
				System.out.println("checkOrder�� �Է��ϼ���  ex) java com.insprit.vmg.checker.obstacle.core.ObstacleDaemon 23");
			} else {
				new ObstacleDaemon().checkObstacle(Integer.parseInt(checkOrder));
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("checkOrder�� �Է��ϼ���");
			errorMsg();
		} catch (NumberFormatException e) {
			System.out.println("(1 ~ 24) ������ ���ڸ� �Է��ϼ���  ex) java com.insprit.vmg.checker.obstacle.core.ObstacleDaemon 23");
			errorMsg();

		} catch (Exception e) {
			System.out.println("�ý��� ���� �����ڿ��� �����ϼ���");
			System.out.println(e);
		} finally{
			System.exit(0);
		}
	}

	private static void errorMsg() {
		System.out.println("0 : ��ü üũ  ");
		System.out.println("1 : Disk Write üũ  ");
		System.out.println("2 : NAS Write üũ");
		System.out.println("3 : Disk �� NAS �뷮 üũ");
		System.out.println("4 : Database Connect üũ");
		System.out.println("5 : Database Insert üũ");
		System.out.println("6 : Database Select üũ");
		System.out.println("7 : Database Delete üũ");
		//System.out.println("8 : SAN �뷮  üũ");
		System.out.println("9 : UAPS ���� üũ");
		//System.out.println("10 : VSMSS ���� üũ");
		System.out.println("11 : IRelay ���� üũ");
		System.out.println("12 : ORelay ���� üũ");
		System.out.println("13 : IGW ���� üũ");
		System.out.println("14 : ���ڸŴ��� ���� üũ");
		System.out.println("15 : SubmitReq ��ȸ üũ");
		System.out.println("16 : UAPS ��ȸ üũ");
		//System.out.println("17 : VSMSS ��ȸ üũ");
		System.out.println("18 : IRelay ��ȸ üũ");
		System.out.println("19 : ORelay ��ȸ üũ");
		//System.out.println("20 : IGW ��ȸ üũ");
		System.out.println("21 : ���ڸŴ��� ��ȸ üũ");
		System.out.println("22 : ���ݷα� ���� üũ");
		System.out.println("23 : Weblogic Alive üũ");
		System.out.println("24 : Apache Alive üũ");
	}
	private void checkObstacle(int checkOrder) {
		System.out.println("################# ��� üũ ����           #################");
		System.out.println("");
		logger.debug("################# ��� üũ ����           #################");
		logger.debug("");
		if ( checkOrder == 0 || checkOrder == 1) checkDiskWrite();
		if ( checkOrder == 0 || checkOrder == 2) checkNasWrite();
		if ( checkOrder == 0 || checkOrder == 3) checkDiskAndNasCapacity();
		if ( checkOrder == 0 || checkOrder == 4) checkDbConnect();
		if ( checkOrder == 0 || checkOrder == 5) checkDbInsert();
		if ( checkOrder == 0 || checkOrder == 6) checkDbSelect();
		if ( checkOrder == 0 || checkOrder == 7) checkDbDelete();
		if ( checkOrder == 0 || checkOrder == 8) checkSanCapacity();
		if ( checkOrder == 0 || checkOrder == 9) checkUapsConnect();
		if ( checkOrder == 0 || checkOrder == 10) checkVsmssConnect();
		if ( checkOrder == 0 || checkOrder == 11) checkIrelayConnect();
		if ( checkOrder == 0 || checkOrder == 12) checkOrelayConnect();
		if ( checkOrder == 0 || checkOrder == 13) checkIgwConnect();
		if ( checkOrder == 0 || checkOrder == 14) checkCharManagerConnect();
		if ( checkOrder == 0 || checkOrder == 15) checkSubmitReqRequest();
		if ( checkOrder == 0 || checkOrder == 16) checkUapsRequest();
		if ( checkOrder == 0 || checkOrder == 17) checkVsmssRequest();
		if ( checkOrder == 0 || checkOrder == 18) checkIrelayRequest();
		if ( checkOrder == 0 || checkOrder == 19) checkOrelayRequest();
		if ( checkOrder == 0 || checkOrder == 20) checkIgwRequest();
		if ( checkOrder == 0 || checkOrder == 21) checkCharManagerRequest();
		if ( checkOrder == 0 || checkOrder == 22) checkMpmd();
		if ( checkOrder == 0 || checkOrder == 23) checkWeblogicAlive();
		if ( checkOrder == 0 || checkOrder == 24) checkApacheAlive();
		System.out.println("");
		System.out.println("");
		System.out.println("################# ��� üũ ��             #################");
		System.out.println("");
		System.out.println("");
		logger.debug("");
		logger.debug("");
		logger.debug("################# ��� üũ ��             #################");
		logger.debug("");
		logger.debug("");
	}

	private void checkDiskWrite() {
		System.out.println("");
		System.out.println("################# Disk Write üũ           #################");
		logger.debug("");
		logger.debug("################# Disk Write üũ           #################");
        FileWriter out;
		try {
			out = new FileWriter("/home/vmg/obstacle.txt",true);
	        out.write("test ");
	        out.close();
	        System.out.println("Disk Write ����");
	        logger.debug("Disk Write ����");

		} catch (IOException e) {
			System.out.println("Disk Write ����");
			System.out.println(e);
			logger.debug("Disk Write ����");
			logger.debug(e);
		}
	}

	private void checkNasWrite() {
		System.out.println("");
		System.out.println("################# NAS Write üũ            #################");
		logger.debug("");
		logger.debug("################# NAS Write üũ            #################");
        FileWriter out;
		try {
			out = new FileWriter("/VMG_MM/obstacle.txt",true);
	        out.write("test ");
	        out.close();
	        System.out.println("NAS Write ����");
	        logger.debug("NAS Write ����");
		} catch (IOException e) {
			System.out.println("NAS Write ����");
			System.out.println(e);
			logger.debug("NAS Write ����");
			logger.debug(e);
		}

	}

	private void checkDiskAndNasCapacity() {
		System.out.println("");
		System.out.println("################# Disk �� NAS �뷮 üũ     #################");
		System.out.println("\r\n"+getResultStreamRuntimeExec("df -k"));
		logger.debug("");
		logger.debug("################# Disk �� NAS �뷮 üũ     #################");
		logger.debug("\r\n"+getResultStreamRuntimeExec("df -k"));

	}

	private void checkDbConnect() {
		System.out.println("");
		System.out.println("################# Database Connect üũ     #################");
		logger.debug("");
		logger.debug("################# Database Connect üũ     #################");
		if (connecter() == null) {
			System.out.println("Database Connect ����");
			logger.debug("Database Connect ����");
		} else {
			System.out.println("Database Connect ����");
			logger.debug("Database Connect ����");
			closer();
		}
	}

	private void checkDbInsert() {
		System.out.println("");
		System.out.println("################# Database Insert üũ      #################");
		logger.debug("");
		logger.debug("################# Database Insert üũ      #################");
        try {
        	connecter();
        	statement = connection.createStatement();
            String checkDbInsert = "INSERT INTO USER_SPAM( SEQ, RECV_MDN, SEND_MDN) VALUES ('9999999999','9999999999','9999999999' )";
        	int result = statement.executeUpdate(checkDbInsert);
        	if ( result == 1) {
        		System.out.println("Database Insert ����");
        		logger.debug("Database Insert ����");
        	} else {
        		System.out.println("Database Insert ����");
        		logger.debug("Database Insert ����");
        	}
		} catch (SQLException e) {
			System.out.println("Database Insert ����");
			System.out.println(e);
			logger.debug("Database Insert ����");
			logger.debug(e);
		} finally {
			closer();
		}
	}

	private void checkDbSelect() {
		System.out.println("");
		System.out.println("################# Database Select üũ      #################");
		logger.debug("");
		logger.debug("################# Database Select üũ      #################");
        try {
        	connecter();
        	statement = connection.createStatement();
            String checkDbSelect = "SELECT seq FROM USER_SPAM WHERE SEQ ='9999999999'";
            rs = statement.executeQuery(checkDbSelect);
        	if ( rs != null) {
        		System.out.println("Database Select ����");
        		logger.debug("Database Select ����");
        	} else {
        		System.out.println("Database Select ����");
        		logger.debug("Database Select ����");
        	}
		} catch (SQLException e) {
			System.out.println("Database Select ����");
			System.out.println(e);
			logger.debug("Database Select ����");
			logger.debug(e);
		} finally {
			closer();
		}
	}


	private void checkDbDelete() {
		System.out.println("");
		System.out.println("################# Database Delete üũ      #################");
		logger.debug("");
		logger.debug("################# Database Delete üũ      #################");
        try {
        	connecter();
        	statement = connection.createStatement();
            String checkDbDelete = "DELETE USER_SPAM WHERE SEQ ='9999999999'";
        	int result = statement.executeUpdate(checkDbDelete);
        	if ( result == 1) {
        		System.out.println("Database Delete ����");
        		logger.debug("Database Delete ����");
        	} else {
        		System.out.println("Database Delete ����");
        		logger.debug("Database Delete ����");
        	}
		} catch (SQLException e) {
			System.out.println("Database Delete ����");
			System.out.println(e);
			logger.debug("Database Delete ����");
			logger.debug(e);
		} finally {
			closer();
		}

	}

	private void checkSanCapacity() {
//		System.out.println("");
//		System.out.println("################# SAN �뷮 üũ             #################");
//		logger.debug("");
//		logger.debug("################# SAN �뷮 üũ             #################");
		// TODO Auto-generated method stub

	}

	private void checkUapsConnect() {
		System.out.println("");
		System.out.println("################# UAPS ���� üũ            #################");
		logger.debug("");
		logger.debug("################# UAPS ���� üũ            #################");
		try {
			SearchChgPlusProfile search = new SearchChgPlusProfile();
			CustProfileInfo user = search.getChgPlusProfileInfo("01093001464", 11);
			if ( user != null) {
				System.out.println("UAPS ���� ����");
				logger.debug("UAPS ���� ����");
			} else {
				System.out.println("UAPS ���� ����");
				logger.debug("UAPS ���� ����");
			}
		} catch (Exception e) {
			System.out.println("UAPS ���� ����");
			System.out.println(e);
			logger.debug("UAPS ���� ����");
			logger.debug(e);
		}

	}

	private void checkVsmssConnect() {
//		System.out.println("");
//		System.out.println("################# VSMSS ���� üũ           #################");
//		logger.debug("");
//		logger.debug("################# VSMSS ���� üũ           #################");
//		boolean isVsmss = false;
//		try {
//			String hostname = java.net.InetAddress.getLocalHost().getHostName().toUpperCase();
//			List vsmssIp = config.getList(hostname+".VSMSS_IP");
//			List vsmssPort = config.getList(hostname+".VSMSS_PORT");
//			for(int i=0; i<vsmssIp.size();i++) {
//				try {
//					Socket vsmssSocket = new Socket((String) vsmssIp.get(i), Integer.parseInt((String)vsmssPort.get(i)));
//					isVsmss = true;
//					if (vsmssSocket != null) vsmssSocket.close();
//					break;
//				} catch (Exception e) {
//				} finally {
//
//				}
//			}
//			if ( isVsmss == true) {
//				System.out.println("VSMSS ���� ����");
//				logger.debug("VSMSS ���� ����");
//			} else {
//				System.out.println("VSMSS ���� ����");
//				logger.debug("VSMSS ���� ����");
//			}
//		} catch (UnknownHostException e) {
//			System.out.println("VSMSS ���� ���� (�� �� ���� ���� ���� �м��� �ʿ���)");
//			System.out.println(e);
//			logger.debug("VSMSS ���� ���� (�� �� ���� ���� ���� �м��� �ʿ���)");
//			logger.debug(e);
//		}
	}

	private void checkIrelayConnect() {
		System.out.println("");
		System.out.println("################# IRelay ���� üũ          #################");
		logger.debug("");
		logger.debug("################# IRelay ���� üũ          #################");
		try {
			String irelayIp = config.getString("IRELAY_IP");
			int irelayPort = config.getInt("IRELAY_PORT");
			try {
				Socket irelaySocket = new Socket(irelayIp, irelayPort);
				irelaySocket.close();
				System.out.println("IRelay ���� ����");
				logger.debug("IRelay ���� ����");
			} catch (Exception e) {
				System.out.println("IRelay ���� ����");
				logger.debug("IRelay ���� ����");
			}
		} catch (Exception e) {
			System.out.println("IRelay ���� ���� (�� �� ���� ���� ���� �м��� �ʿ���)");
			System.out.println(e);
			logger.debug("IRelay ���� ���� (�� �� ���� ���� ���� �м��� �ʿ���)");
			logger.debug(e);
		}

	}

	private void checkOrelayConnect() {
		System.out.println("");
		System.out.println("################# ORelay ����  üũ         #################");
		logger.debug("");
		logger.debug("################# ORelay ����  üũ         #################");
		try {
			String orelayIp = config.getString("ORELAY_IP");
			int orelayPort = config.getInt("ORELAY_PORT");
			try {
				Socket orelaySocket = new Socket(orelayIp, orelayPort);
				orelaySocket.close();
				System.out.println("ORelay ���� ����");
				logger.debug("ORelay ���� ����");
			} catch (Exception e) {
				System.out.println("ORelay ���� ����");
				logger.debug("ORelay ���� ����");
			}
		} catch (Exception e) {
			System.out.println("ORelay ���� ���� (�� �� ���� ���� ���� �м��� �ʿ���)");
			System.out.println(e);
			logger.debug("ORelay ���� ���� (�� �� ���� ���� ���� �м��� �ʿ���)");
			logger.debug(e);
		}

	}

	private void checkIgwConnect() {
		System.out.println("");
		System.out.println("################# IGW ���� üũ             #################");
		logger.debug("");
		logger.debug("################# IGW ���� üũ             #################");
		boolean isIgw = false;
		try {
			List igwIp = config.getList("IGW_IP");
			List igwPort = config.getList("IGW_PORT");
			for(int i=0; i<igwIp.size();i++) {
				try {
					Socket igwSocket = new Socket((String) igwIp.get(i), Integer.parseInt((String)igwPort.get(i)));
					isIgw = true;
					igwSocket.close();
				} catch (Exception e) {
				}
			}
			if ( isIgw == true) {
				System.out.println("IGW ���� ����");
				logger.debug("IGW ���� ����");
			} else {
				System.out.println("IGW ���� ����");
				logger.debug("IGW ���� ����");
			}
		} catch (Exception e) {
			System.out.println("IGW ���� ���� (�� �� ���� ���� ���� �м��� �ʿ���)");
			System.out.println(e);
			logger.debug("IGW ���� ���� (�� �� ���� ���� ���� �м��� �ʿ���)");
			logger.debug(e);
		}

	}

	private void checkCharManagerConnect() {
		System.out.println("");
		System.out.println("################# ���ڸŴ��� ���� üũ      #################");
		logger.debug("");
		logger.debug("################# ���ڸŴ��� ���� üũ      #################");
		try {
			String charIp = config.getString("CHAR_IP");
			int charPort = config.getInt("CHAR_PORT");
			try {
				Socket charSocket = new Socket(charIp, charPort);
				charSocket.close();
				System.out.println("���ڸŴ��� ���� ����");
				logger.debug("���ڸŴ��� ���� ����");
			} catch (Exception e) {
				System.out.println("���ڸŴ��� ���� ����");
				logger.debug("���ڸŴ��� ���� ����");
			}
		} catch (Exception e) {
			System.out.println("���ڸŴ��� ���� ���� (�� �� ���� ���� ���� �м��� �ʿ���)");
			System.out.println(e);
			logger.debug("���ڸŴ��� ���� ���� (�� �� ���� ���� ���� �м��� �ʿ���)");
			logger.debug(e);
		}

	}

	private void checkSubmitReqRequest() {
		System.out.println("");
		System.out.println("################# SubmitReq ��ȸ üũ       #################");
		logger.debug("");
		logger.debug("################# SubmitReq ��ȸ üũ       #################");
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams().setConnectionTimeout(60000);
		String contentType = "multipart/related; boundary=\"NextPart_000_0028_01C19839.84698430\"; type=\"text/xml\"; start=\"<start_MM7_SOAP>\"";
		PostMethod post = null;
		try {
			File input = new File("data/onlytext.xml");
			String soap = FileUtils.readFileToString(input);
			post = new PostMethod(config.getString("SUBMITREQ_POSTMETHOD"));
			post.setRequestHeader("Content-Type", contentType);
			post.setRequestHeader("Content-Length", String.valueOf(soap.getBytes().length));
			post.setRequestHeader("Connection", "Close");
			post.setRequestHeader("SOAPAction", "");
			post.setRequestBody(new ByteArrayInputStream(soap.getBytes()));
			int result = client.executeMethod(post);
			if ( result == 200) {
				System.out.println("SubmitReq ��ȸ ����");
				logger.debug("SubmitReq ��ȸ ����");
			} else {
				System.out.println("SubmitReq ��ȸ ����");
				logger.debug("SubmitReq ��ȸ ����");
			}
			//System.out.println("Response Value: ");
			//System.out.println(post.getResponseBodyAsString());
		} catch (Exception ex) {
			System.out.println("SubmitReq ��ȸ ����");
			System.out.println(ex);
			logger.debug("SubmitReq ��ȸ ����");
			logger.debug(ex);
		} finally {
			post.releaseConnection();
		}

	}

	private void checkUapsRequest() {
		System.out.println("");
		System.out.println("################# UAPS ��ȸ  üũ           #################");
		logger.debug("");
		logger.debug("################# UAPS ��ȸ  üũ           #################");
		try {
			SearchChgPlusProfile search = new SearchChgPlusProfile();
			CustProfileInfo user = search.getChgPlusProfileInfo("01093001464", 11);
			if (user != null) {
				System.out.println("UAPS ��ȸ ����");
				logger.debug("UAPS ��ȸ ����");
			} else {
				System.out.println("UAPS ��ȸ ����");
				logger.debug("UAPS ��ȸ ����");
			}
		} catch (Exception e) {
			System.out.println("UAPS ��ȸ ����");
			System.out.println(e);
			logger.debug("UAPS ��ȸ ����");
			logger.debug(e);
		}
	}

	private void checkVsmssRequest() {
//		System.out.println("");
//		System.out.println("################# VSMSS ��ȸ üũ           #################");
//		logger.debug("");
//		logger.debug("################# VSMSS ��ȸ üũ           #################");
//		boolean isVsmss = false;
//		try {
//			String hostname = java.net.InetAddress.getLocalHost().getHostName().toUpperCase();
//			List vsmssIp = config.getList(hostname+".VSMSS_IP");
//			List vsmssPort = config.getList(hostname+".VSMSS_PORT");
//			List vsmssCid = config.getList(hostname+".VSMSS_CID");
//
//			for(int i=0; i<vsmssIp.size();i++) {
//				try {
//					Socket vsmssSocket = new Socket((String) vsmssIp.get(i), Integer.parseInt((String)vsmssPort.get(i)));
//
//					OutputStream vsmssOutput = vsmssSocket.getOutputStream();
//					InputStream vsmssInput = vsmssSocket.getInputStream();
//
//			        vsmssOutput.write(makeVsmssPacket( (String) vsmssCid.get(i)));
//			        vsmssOutput.flush();
//
//			        byte readRequest[] = new byte [ 120 ];
//
//			        if ( vsmssInput.read( readRequest) > 0 ) {
//			        	isVsmss = true;
//						if( vsmssInput != null ) vsmssInput.close();
//						if( vsmssOutput != null ) vsmssOutput.close();
//						if( vsmssSocket != null ) vsmssSocket.close();
//			        	break;
//
//			        }
//				} catch (Exception e) {
//
//				}
//			}
//			if ( isVsmss == true) {
//				System.out.println("VSMSS ��ȸ ����");
//				logger.debug("VSMSS ��ȸ ����");
//			} else {
//				System.out.println("VSMSS ��ȸ ����");
//				logger.debug("VSMSS ��ȸ ����");
//			}
//		} catch (UnknownHostException e) {
//			System.out.println("VSMSS ��ȸ ���� (�� �� ���� ���� ���� �м��� �ʿ���)");
//			System.out.println(e);
//			logger.debug("VSMSS ��ȸ ���� (�� �� ���� ���� ���� �м��� �ʿ���)");
//			logger.debug(e);
//		}
	}

	private void checkIrelayRequest() {
		System.out.println("");
		System.out.println("################# IRelay ��ȸ üũ          #################");
		logger.debug("");
		logger.debug("################# IRelay ��ȸ üũ          #################");
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams().setConnectionTimeout(60000);
		String contentType = "type=\"text/xml\"; charset=\"euc-kr\"";
		PostMethod post = null;
		try {
			File input = new File("data/irelaySample.xml");
			String soap = FileUtils.readFileToString(input);
			post = new PostMethod(config.getString("IRELAY_POSTMETHOD"));
			post.setRequestHeader("HOST", "mmsc");
			post.setRequestHeader("Content-Type", contentType);
			post.setRequestHeader("X-SKT-MM4-Transaction", "MM4_Forward.req");
			post.setRequestHeader("Content-Length", String.valueOf(soap.getBytes().length));
			post.setRequestHeader("Connection", "Close");
			post.setRequestBody(new ByteArrayInputStream(soap.getBytes()));
			int result = client.executeMethod(post);
			if ( result == 200) {
				System.out.println("IRelay ��ȸ ����");
				logger.debug("IRelay ��ȸ ����");
			} else {
				System.out.println("IRelay ��ȸ ����");
				logger.debug("IRelay ��ȸ ����");
			}
			//System.out.println("Response Value: ");
			//System.out.println(post.getResponseBodyAsString());
		} catch (Exception ex) {
			System.out.println("IRelay ��ȸ ����");
			System.out.println(ex);
			logger.debug("IRelay ��ȸ ����");
			logger.debug(ex);
		} finally {
			post.releaseConnection();
		}

	}

	private void checkOrelayRequest() {
		System.out.println("");
		System.out.println("################# ORelay ��ȸ  üũ         #################");
		logger.debug("");
		logger.debug("################# ORelay ��ȸ  üũ         #################");
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams().setConnectionTimeout(60000);
		String contentType = "multipart/related; boundary=\"HTTP_INSPRIT_20090518152154_I_r07771208\"; type=\"text/xml\"; start=\"<start_MM7_SOAP>\"";
		PostMethod post = null;
		try {
			File input = new File("data/orelaySample.xml");
			String soap = FileUtils.readFileToString(input);
			post = new PostMethod(config.getString("ORELAY_POSTMETHOD"));
			post.setRequestHeader("Content-Type", contentType);
			post.setRequestHeader("Content-Length", String.valueOf(soap.getBytes().length));
			post.setRequestHeader("Connection", "Close");
			post.setRequestHeader("SOAPAction", "");
			post.setRequestBody(new ByteArrayInputStream(soap.getBytes()));
			int result = client.executeMethod(post);
			if ( result == 200) {
				System.out.println("ORelay ��ȸ ����");
				logger.debug("ORelay ��ȸ ����");
			} else {
				System.out.println("ORelay ��ȸ ����");
				logger.debug("ORelay ��ȸ ����");
			}
			//System.out.println("Response Value: ");
			//System.out.println(post.getResponseBodyAsString());
		} catch (Exception ex) {
			System.out.println("ORelay ��ȸ ����");
			System.out.println(ex);
			logger.debug("ORelay ��ȸ ����");
			logger.debug(ex);
		} finally {
			post.releaseConnection();
		}

	}

	private void checkIgwRequest() {
//		System.out.println("");
//		System.out.println("################# IGW ��ȸ üũ             #################");
//		logger.debug("");
//		logger.debug("################# IGW ��ȸ üũ             #################");
		// TODO Auto-generated method stub

	}

	private void checkCharManagerRequest() {
		System.out.println("");
		System.out.println("################# ���ڸŴ��� ��ȸ üũ      #################");
		logger.debug("");
		logger.debug("################# ���ڸŴ��� ��ȸ üũ      #################");
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams().setConnectionTimeout(60000);
//		String contentType = "multipart/related; boundary=\"HTTP_INSPRIT_20090507110045_C_r07163126_01091215582\"; type=\"text/xml\"; start=\"<start_MM7_SOAP>\"";
		String contentType = "multipart/related; boundary=\"HTTP_INSPRIT_20090518152154_I_r07771208\"; type=text/xml; start=\"<start_MM7_SOAP>\"";
		PostMethod post = null;
		try {
			File input = new File("data/charManagerSample.xml");
			String soap = FileUtils.readFileToString(input);
			post = new PostMethod(config.getString("CHAR_POSTMETHOD"));
			post.setRequestHeader("Content-Type", contentType);
			post.setRequestHeader("Content-Length", String.valueOf(soap.getBytes().length));
			post.setRequestHeader("Connection", "Close");
			post.setRequestHeader("SOAPAction", "");
			post.setRequestBody(new ByteArrayInputStream(soap.getBytes()));
			int result = client.executeMethod(post);
			if ( result == 200) {
				System.out.println("���ڸŴ��� ��ȸ ����");
				logger.debug("���ڸŴ��� ��ȸ ����");
			} else {
				System.out.println("���ڸŴ��� ��ȸ ����");
				logger.debug("���ڸŴ��� ��ȸ ����");
			}
			//System.out.println("Response Value: ");
			//System.out.println(post.getResponseBodyAsString());
		} catch (Exception ex) {
			System.out.println("���ڸŴ��� ��ȸ ����");
			System.out.println(ex);
			logger.debug("���ڸŴ��� ��ȸ ����");
			logger.debug(ex);
		} finally {
			post.releaseConnection();
		}


	}

	private void checkMpmd() {
		System.out.println("");
		System.out.println("################# ���ݷα� ���� üũ        #################");
		logger.debug("");
		logger.debug("################# ���ݷα� ���� üũ        #################");
		boolean checkMpmdFlag = false;
        try {
            File root = new File("/log2/MPMD/DATA");
            File[] fl = root.listFiles();

            for(int i=0; i<fl.length; i++){
                //System.out.println("filename="+fl[i].getName());
                long lastModify = fl[i].lastModified();
                //System.out.println("lastModi="+fl[i].lastModified());
                long time = System.currentTimeMillis ( );
                //System.out.println ( "currTime=" + time );
                //System.out.println ( "interval=" + ((time -  lastModify) / 1000.0 ));
                if ( ((time -  lastModify) / 1000.0 ) < 300 ) checkMpmdFlag = true;
            }
            if (checkMpmdFlag) {
            	System.out.println("���ݷα� ���� ����");
            	logger.debug("���ݷα� ���� ����");
            } else {
            	System.out.println("���ݷα� ���� ����");
            	logger.debug("���ݷα� ���� ����");
            }
        } catch (Exception e) {
        	System.out.println("���ݷα� ���� ���� (�� �� ���� ���� ���� �м��� �ʿ���)");
        	logger.debug(e);
        	System.out.println("���ݷα� ���� ���� (�� �� ���� ���� ���� �м��� �ʿ���)");
        	logger.debug(e);

        }

	}

	private void checkWeblogicAlive() {
		System.out.println("");
		System.out.println("################# Weblogic Alive üũ       #################");
		logger.debug("");
		logger.debug("################# Weblogic Alive üũ       #################");
		if (!getResultStreamRuntimeExec("/app/vmg/bin/COUNT-WAS").equals("0")) {
			System.out.println("Weblogic Alive ����");
			logger.debug("Weblogic Alive ����");
		} else {
			System.out.println("Weblogic Alive ����");
			logger.debug("Weblogic Alive ����");
		}
	}

	private void checkApacheAlive() {
		System.out.println("");
		System.out.println("################# Apache Alive üũ         #################");
		logger.debug("");
		logger.debug("################# Apache Alive üũ         #################");
		if (!getResultStreamRuntimeExec("/app/vmg/bin/COUNT-APACHE").equals("0")) {
			System.out.println("Apache Alive ����");
			logger.debug("Apache Alive ����");
		} else {
			System.out.println("Apache Alive ����");
			logger.debug("Apache Alive ����");
		}
	}

	public boolean isRuntimeExec(String command) {
		try {
			Process p = Runtime.getRuntime().exec( command );
			BufferedReader input = new BufferedReader (new InputStreamReader(p.getInputStream()));
	        p.waitFor();
			input.close();
			return true;
		} catch ( Exception e) {
			System.out.println( "Exception : " + e);
			return false;
		}
	}

	public String getResultStreamRuntimeExec(String command) {
		StringBuffer resultData = new StringBuffer();
		BufferedReader input = null;
		try {
			Process p = Runtime.getRuntime().exec( command );
			input = new BufferedReader (new InputStreamReader(p.getInputStream()));
			int c;
			while ((c = input.read()) != -1) {
				resultData.append((char) c);
			}
			return resultData.toString().trim();
		} catch ( Exception e) {
			System.out.println( "Exception : " + e);
			return null;
		} finally {
			 try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

    public Connection connecter() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            String db_url = config.getString("DB_URL");
            connection = DriverManager.getConnection(db_url, config.getString("DB_ID"), config.getString("DB_PASS"));
            return connection;
        } catch (Exception e) {
        	System.out.println(e);
            return null;
        }

    }

    public void closer()  {
        try {
        	connection.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    public byte[] makeVsmssPacket( String CID ) {

    	byte[] dest = null;
        try{
        	String message = "VMG ���� �����׽�Ʈ�Դϴ� - " + CID;
            dest = new byte[276];

            dest = setInt(dest, 0, 0x04);

            dest = setString(dest, 4, CID, 16);
            dest = setInt(dest, 20, 0);
            dest = setInt(dest, 24, 0);
            dest = setString(dest, 28, "010", 16);
            dest = setInt(dest, 44, 93001464);
            dest = setInt(dest, 48, 0);
            dest = setShort(dest, 52, (short)11);
            dest = setShort(dest, 54, (short)10);
            dest = setShort(dest, 56, (short)0);
            dest = setShort(dest, 58, (short) 0);

            dest = setInt(dest, 60, 156);
            dest = setInt(dest, 64, 100754642);

            dest[68] = (byte)49;
            dest[69] = (byte)1;
            dest[70] = (byte)0;
            dest[71] = (byte)0;
            dest = setInt(dest, 72, 0);
            dest = setInt(dest, 76, 0);

            dest = setString(dest, 80, getTime("yyMMddHHmm"), 11);
            dest = setString(dest, 91, "", 29);

            dest = setInt(dest, 120, 86400);
            dest[124] = (byte)2;

            dest = setString(dest, 125, "", 21);
            dest[146] = (byte)message.getBytes().length ;
            System.arraycopy(fillNull(message.getBytes(), 128), 0, dest, 147, 128);

            dest[275] = (byte)0;

        } catch ( Exception a ) {
        }
        return dest;
        }


    public byte[] setInt( byte[] dest, int offset, int i ) {
        dest[offset+3] = (byte)(i & 0xff);
        dest[offset+2] = (byte)((i>>8) & 0xff);
        dest[offset+1] = (byte)((i>>16) & 0xff);
        dest[offset] = (byte)((i>>24) & 0xff);
        return dest;
    }

    public byte[] setShort( byte[] dest, int offset, short s ) {
        dest[offset+1] = (byte)(s & 0xff);
        dest[offset] = (byte)((s>>8) & 0xff);
        return dest;
    }

    public String getTime(String formatStr) {
        Calendar cal = Calendar.getInstance();
        long lTime = cal.getTime().getTime();

        if(lTime == 0)
            return "";

        if(formatStr == null || formatStr.length() == 0)
            formatStr = "yyyy-MM-dd HH:mm";

        SimpleDateFormat formatter = new SimpleDateFormat(formatStr);

        return formatter.format(new java.util.Date(lTime));
    }

    public byte[] fillNull(byte[] src, int length) {
        byte[] result = new byte[length];

        if (src.length > result.length) {
            System.arraycopy(src, 0, result, 0, result.length);
        } else {
            System.arraycopy(src, 0, result, 0, src.length);

            for (int i = src.length; i < length; i++) {
                result[i] = 0x00;
            }
        }

        return result;
    }

    public byte[] setString(byte[] destBuff, int offset, String srcData, int maxLength) {
        byte[] srcBuff = srcData.getBytes();

        for(int i=offset; i<offset+maxLength; i++) {
            if(i < offset+srcBuff.length) {
                destBuff[i] = srcBuff[i-offset];
            } else {
                destBuff[i] = (byte)0;
            }
        }
        return destBuff;
    }

}

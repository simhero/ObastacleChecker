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
 * @author 심병철
 * @author <a href="mailto:simhero@in-sprit.com">심병철</a>
 * @author <a href="mailto:simhero@gmail.com">심병철</a>
 * @version $Id: ObstacleDaemon.java,v 1.0 2009/05/07 00:00:00 심병철 Express $
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
				System.out.println("checkOrder를 입력하세요  ex) java com.insprit.vmg.checker.obstacle.core.ObstacleDaemon 23");
			} else {
				new ObstacleDaemon().checkObstacle(Integer.parseInt(checkOrder));
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("checkOrder를 입력하세요");
			errorMsg();
		} catch (NumberFormatException e) {
			System.out.println("(1 ~ 24) 사이의 숫자를 입력하세요  ex) java com.insprit.vmg.checker.obstacle.core.ObstacleDaemon 23");
			errorMsg();

		} catch (Exception e) {
			System.out.println("시스템 에러 관리자에게 문의하세요");
			System.out.println(e);
		} finally{
			System.exit(0);
		}
	}

	private static void errorMsg() {
		System.out.println("0 : 전체 체크  ");
		System.out.println("1 : Disk Write 체크  ");
		System.out.println("2 : NAS Write 체크");
		System.out.println("3 : Disk 및 NAS 용량 체크");
		System.out.println("4 : Database Connect 체크");
		System.out.println("5 : Database Insert 체크");
		System.out.println("6 : Database Select 체크");
		System.out.println("7 : Database Delete 체크");
		//System.out.println("8 : SAN 용량  체크");
		System.out.println("9 : UAPS 연결 체크");
		//System.out.println("10 : VSMSS 연결 체크");
		System.out.println("11 : IRelay 연결 체크");
		System.out.println("12 : ORelay 연결 체크");
		System.out.println("13 : IGW 연결 체크");
		System.out.println("14 : 문자매니저 연결 체크");
		System.out.println("15 : SubmitReq 조회 체크");
		System.out.println("16 : UAPS 조회 체크");
		//System.out.println("17 : VSMSS 조회 체크");
		System.out.println("18 : IRelay 조회 체크");
		System.out.println("19 : ORelay 조회 체크");
		//System.out.println("20 : IGW 조회 체크");
		System.out.println("21 : 문자매니저 조회 체크");
		System.out.println("22 : 과금로그 생성 체크");
		System.out.println("23 : Weblogic Alive 체크");
		System.out.println("24 : Apache Alive 체크");
	}
	private void checkObstacle(int checkOrder) {
		System.out.println("################# 장애 체크 시작           #################");
		System.out.println("");
		logger.debug("################# 장애 체크 시작           #################");
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
		System.out.println("################# 장애 체크 끝             #################");
		System.out.println("");
		System.out.println("");
		logger.debug("");
		logger.debug("");
		logger.debug("################# 장애 체크 끝             #################");
		logger.debug("");
		logger.debug("");
	}

	private void checkDiskWrite() {
		System.out.println("");
		System.out.println("################# Disk Write 체크           #################");
		logger.debug("");
		logger.debug("################# Disk Write 체크           #################");
        FileWriter out;
		try {
			out = new FileWriter("/home/vmg/obstacle.txt",true);
	        out.write("test ");
	        out.close();
	        System.out.println("Disk Write 정상");
	        logger.debug("Disk Write 정상");

		} catch (IOException e) {
			System.out.println("Disk Write 실패");
			System.out.println(e);
			logger.debug("Disk Write 실패");
			logger.debug(e);
		}
	}

	private void checkNasWrite() {
		System.out.println("");
		System.out.println("################# NAS Write 체크            #################");
		logger.debug("");
		logger.debug("################# NAS Write 체크            #################");
        FileWriter out;
		try {
			out = new FileWriter("/VMG_MM/obstacle.txt",true);
	        out.write("test ");
	        out.close();
	        System.out.println("NAS Write 정상");
	        logger.debug("NAS Write 정상");
		} catch (IOException e) {
			System.out.println("NAS Write 실패");
			System.out.println(e);
			logger.debug("NAS Write 실패");
			logger.debug(e);
		}

	}

	private void checkDiskAndNasCapacity() {
		System.out.println("");
		System.out.println("################# Disk 및 NAS 용량 체크     #################");
		System.out.println("\r\n"+getResultStreamRuntimeExec("df -k"));
		logger.debug("");
		logger.debug("################# Disk 및 NAS 용량 체크     #################");
		logger.debug("\r\n"+getResultStreamRuntimeExec("df -k"));

	}

	private void checkDbConnect() {
		System.out.println("");
		System.out.println("################# Database Connect 체크     #################");
		logger.debug("");
		logger.debug("################# Database Connect 체크     #################");
		if (connecter() == null) {
			System.out.println("Database Connect 실패");
			logger.debug("Database Connect 실패");
		} else {
			System.out.println("Database Connect 성공");
			logger.debug("Database Connect 성공");
			closer();
		}
	}

	private void checkDbInsert() {
		System.out.println("");
		System.out.println("################# Database Insert 체크      #################");
		logger.debug("");
		logger.debug("################# Database Insert 체크      #################");
        try {
        	connecter();
        	statement = connection.createStatement();
            String checkDbInsert = "INSERT INTO USER_SPAM( SEQ, RECV_MDN, SEND_MDN) VALUES ('9999999999','9999999999','9999999999' )";
        	int result = statement.executeUpdate(checkDbInsert);
        	if ( result == 1) {
        		System.out.println("Database Insert 성공");
        		logger.debug("Database Insert 성공");
        	} else {
        		System.out.println("Database Insert 실패");
        		logger.debug("Database Insert 실패");
        	}
		} catch (SQLException e) {
			System.out.println("Database Insert 실패");
			System.out.println(e);
			logger.debug("Database Insert 실패");
			logger.debug(e);
		} finally {
			closer();
		}
	}

	private void checkDbSelect() {
		System.out.println("");
		System.out.println("################# Database Select 체크      #################");
		logger.debug("");
		logger.debug("################# Database Select 체크      #################");
        try {
        	connecter();
        	statement = connection.createStatement();
            String checkDbSelect = "SELECT seq FROM USER_SPAM WHERE SEQ ='9999999999'";
            rs = statement.executeQuery(checkDbSelect);
        	if ( rs != null) {
        		System.out.println("Database Select 성공");
        		logger.debug("Database Select 성공");
        	} else {
        		System.out.println("Database Select 실패");
        		logger.debug("Database Select 실패");
        	}
		} catch (SQLException e) {
			System.out.println("Database Select 실패");
			System.out.println(e);
			logger.debug("Database Select 실패");
			logger.debug(e);
		} finally {
			closer();
		}
	}


	private void checkDbDelete() {
		System.out.println("");
		System.out.println("################# Database Delete 체크      #################");
		logger.debug("");
		logger.debug("################# Database Delete 체크      #################");
        try {
        	connecter();
        	statement = connection.createStatement();
            String checkDbDelete = "DELETE USER_SPAM WHERE SEQ ='9999999999'";
        	int result = statement.executeUpdate(checkDbDelete);
        	if ( result == 1) {
        		System.out.println("Database Delete 성공");
        		logger.debug("Database Delete 성공");
        	} else {
        		System.out.println("Database Delete 실패");
        		logger.debug("Database Delete 실패");
        	}
		} catch (SQLException e) {
			System.out.println("Database Delete 실패");
			System.out.println(e);
			logger.debug("Database Delete 실패");
			logger.debug(e);
		} finally {
			closer();
		}

	}

	private void checkSanCapacity() {
//		System.out.println("");
//		System.out.println("################# SAN 용량 체크             #################");
//		logger.debug("");
//		logger.debug("################# SAN 용량 체크             #################");
		// TODO Auto-generated method stub

	}

	private void checkUapsConnect() {
		System.out.println("");
		System.out.println("################# UAPS 연결 체크            #################");
		logger.debug("");
		logger.debug("################# UAPS 연결 체크            #################");
		try {
			SearchChgPlusProfile search = new SearchChgPlusProfile();
			CustProfileInfo user = search.getChgPlusProfileInfo("01093001464", 11);
			if ( user != null) {
				System.out.println("UAPS 연결 성공");
				logger.debug("UAPS 연결 성공");
			} else {
				System.out.println("UAPS 연결 실패");
				logger.debug("UAPS 연결 실패");
			}
		} catch (Exception e) {
			System.out.println("UAPS 연결 실패");
			System.out.println(e);
			logger.debug("UAPS 연결 실패");
			logger.debug(e);
		}

	}

	private void checkVsmssConnect() {
//		System.out.println("");
//		System.out.println("################# VSMSS 연결 체크           #################");
//		logger.debug("");
//		logger.debug("################# VSMSS 연결 체크           #################");
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
//				System.out.println("VSMSS 연결 성공");
//				logger.debug("VSMSS 연결 성공");
//			} else {
//				System.out.println("VSMSS 연결 실패");
//				logger.debug("VSMSS 연결 실패");
//			}
//		} catch (UnknownHostException e) {
//			System.out.println("VSMSS 연결 실패 (좀 더 상세한 에러 원인 분석이 필요함)");
//			System.out.println(e);
//			logger.debug("VSMSS 연결 실패 (좀 더 상세한 에러 원인 분석이 필요함)");
//			logger.debug(e);
//		}
	}

	private void checkIrelayConnect() {
		System.out.println("");
		System.out.println("################# IRelay 연결 체크          #################");
		logger.debug("");
		logger.debug("################# IRelay 연결 체크          #################");
		try {
			String irelayIp = config.getString("IRELAY_IP");
			int irelayPort = config.getInt("IRELAY_PORT");
			try {
				Socket irelaySocket = new Socket(irelayIp, irelayPort);
				irelaySocket.close();
				System.out.println("IRelay 연결 성공");
				logger.debug("IRelay 연결 성공");
			} catch (Exception e) {
				System.out.println("IRelay 연결 실패");
				logger.debug("IRelay 연결 실패");
			}
		} catch (Exception e) {
			System.out.println("IRelay 연결 실패 (좀 더 상세한 에러 원인 분석이 필요함)");
			System.out.println(e);
			logger.debug("IRelay 연결 실패 (좀 더 상세한 에러 원인 분석이 필요함)");
			logger.debug(e);
		}

	}

	private void checkOrelayConnect() {
		System.out.println("");
		System.out.println("################# ORelay 연결  체크         #################");
		logger.debug("");
		logger.debug("################# ORelay 연결  체크         #################");
		try {
			String orelayIp = config.getString("ORELAY_IP");
			int orelayPort = config.getInt("ORELAY_PORT");
			try {
				Socket orelaySocket = new Socket(orelayIp, orelayPort);
				orelaySocket.close();
				System.out.println("ORelay 연결 성공");
				logger.debug("ORelay 연결 성공");
			} catch (Exception e) {
				System.out.println("ORelay 연결 실패");
				logger.debug("ORelay 연결 실패");
			}
		} catch (Exception e) {
			System.out.println("ORelay 연결 실패 (좀 더 상세한 에러 원인 분석이 필요함)");
			System.out.println(e);
			logger.debug("ORelay 연결 실패 (좀 더 상세한 에러 원인 분석이 필요함)");
			logger.debug(e);
		}

	}

	private void checkIgwConnect() {
		System.out.println("");
		System.out.println("################# IGW 연결 체크             #################");
		logger.debug("");
		logger.debug("################# IGW 연결 체크             #################");
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
				System.out.println("IGW 연결 성공");
				logger.debug("IGW 연결 성공");
			} else {
				System.out.println("IGW 연결 실패");
				logger.debug("IGW 연결 실패");
			}
		} catch (Exception e) {
			System.out.println("IGW 연결 실패 (좀 더 상세한 에러 원인 분석이 필요함)");
			System.out.println(e);
			logger.debug("IGW 연결 실패 (좀 더 상세한 에러 원인 분석이 필요함)");
			logger.debug(e);
		}

	}

	private void checkCharManagerConnect() {
		System.out.println("");
		System.out.println("################# 문자매니저 연결 체크      #################");
		logger.debug("");
		logger.debug("################# 문자매니저 연결 체크      #################");
		try {
			String charIp = config.getString("CHAR_IP");
			int charPort = config.getInt("CHAR_PORT");
			try {
				Socket charSocket = new Socket(charIp, charPort);
				charSocket.close();
				System.out.println("문자매니저 연결 성공");
				logger.debug("문자매니저 연결 성공");
			} catch (Exception e) {
				System.out.println("문자매니저 연결 실패");
				logger.debug("문자매니저 연결 실패");
			}
		} catch (Exception e) {
			System.out.println("문자매니저 연결 실패 (좀 더 상세한 에러 원인 분석이 필요함)");
			System.out.println(e);
			logger.debug("문자매니저 연결 실패 (좀 더 상세한 에러 원인 분석이 필요함)");
			logger.debug(e);
		}

	}

	private void checkSubmitReqRequest() {
		System.out.println("");
		System.out.println("################# SubmitReq 조회 체크       #################");
		logger.debug("");
		logger.debug("################# SubmitReq 조회 체크       #################");
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
				System.out.println("SubmitReq 조회 성공");
				logger.debug("SubmitReq 조회 성공");
			} else {
				System.out.println("SubmitReq 조회 실패");
				logger.debug("SubmitReq 조회 실패");
			}
			//System.out.println("Response Value: ");
			//System.out.println(post.getResponseBodyAsString());
		} catch (Exception ex) {
			System.out.println("SubmitReq 조회 실패");
			System.out.println(ex);
			logger.debug("SubmitReq 조회 실패");
			logger.debug(ex);
		} finally {
			post.releaseConnection();
		}

	}

	private void checkUapsRequest() {
		System.out.println("");
		System.out.println("################# UAPS 조회  체크           #################");
		logger.debug("");
		logger.debug("################# UAPS 조회  체크           #################");
		try {
			SearchChgPlusProfile search = new SearchChgPlusProfile();
			CustProfileInfo user = search.getChgPlusProfileInfo("01093001464", 11);
			if (user != null) {
				System.out.println("UAPS 조회 성공");
				logger.debug("UAPS 조회 성공");
			} else {
				System.out.println("UAPS 조회 실패");
				logger.debug("UAPS 조회 실패");
			}
		} catch (Exception e) {
			System.out.println("UAPS 조회 실패");
			System.out.println(e);
			logger.debug("UAPS 조회 실패");
			logger.debug(e);
		}
	}

	private void checkVsmssRequest() {
//		System.out.println("");
//		System.out.println("################# VSMSS 조회 체크           #################");
//		logger.debug("");
//		logger.debug("################# VSMSS 조회 체크           #################");
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
//				System.out.println("VSMSS 조회 성공");
//				logger.debug("VSMSS 조회 성공");
//			} else {
//				System.out.println("VSMSS 조회 실패");
//				logger.debug("VSMSS 조회 실패");
//			}
//		} catch (UnknownHostException e) {
//			System.out.println("VSMSS 조회 실패 (좀 더 상세한 에러 원인 분석이 필요함)");
//			System.out.println(e);
//			logger.debug("VSMSS 조회 실패 (좀 더 상세한 에러 원인 분석이 필요함)");
//			logger.debug(e);
//		}
	}

	private void checkIrelayRequest() {
		System.out.println("");
		System.out.println("################# IRelay 조회 체크          #################");
		logger.debug("");
		logger.debug("################# IRelay 조회 체크          #################");
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
				System.out.println("IRelay 조회 성공");
				logger.debug("IRelay 조회 성공");
			} else {
				System.out.println("IRelay 조회 실패");
				logger.debug("IRelay 조회 실패");
			}
			//System.out.println("Response Value: ");
			//System.out.println(post.getResponseBodyAsString());
		} catch (Exception ex) {
			System.out.println("IRelay 조회 실패");
			System.out.println(ex);
			logger.debug("IRelay 조회 실패");
			logger.debug(ex);
		} finally {
			post.releaseConnection();
		}

	}

	private void checkOrelayRequest() {
		System.out.println("");
		System.out.println("################# ORelay 조회  체크         #################");
		logger.debug("");
		logger.debug("################# ORelay 조회  체크         #################");
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
				System.out.println("ORelay 조회 성공");
				logger.debug("ORelay 조회 성공");
			} else {
				System.out.println("ORelay 조회 실패");
				logger.debug("ORelay 조회 실패");
			}
			//System.out.println("Response Value: ");
			//System.out.println(post.getResponseBodyAsString());
		} catch (Exception ex) {
			System.out.println("ORelay 조회 실패");
			System.out.println(ex);
			logger.debug("ORelay 조회 실패");
			logger.debug(ex);
		} finally {
			post.releaseConnection();
		}

	}

	private void checkIgwRequest() {
//		System.out.println("");
//		System.out.println("################# IGW 조회 체크             #################");
//		logger.debug("");
//		logger.debug("################# IGW 조회 체크             #################");
		// TODO Auto-generated method stub

	}

	private void checkCharManagerRequest() {
		System.out.println("");
		System.out.println("################# 문자매니저 조회 체크      #################");
		logger.debug("");
		logger.debug("################# 문자매니저 조회 체크      #################");
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
				System.out.println("문자매니저 조회 성공");
				logger.debug("문자매니저 조회 성공");
			} else {
				System.out.println("문자매니저 조회 실패");
				logger.debug("문자매니저 조회 실패");
			}
			//System.out.println("Response Value: ");
			//System.out.println(post.getResponseBodyAsString());
		} catch (Exception ex) {
			System.out.println("문자매니저 조회 실패");
			System.out.println(ex);
			logger.debug("문자매니저 조회 실패");
			logger.debug(ex);
		} finally {
			post.releaseConnection();
		}


	}

	private void checkMpmd() {
		System.out.println("");
		System.out.println("################# 과금로그 생성 체크        #################");
		logger.debug("");
		logger.debug("################# 과금로그 생성 체크        #################");
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
            	System.out.println("과금로그 생성 성공");
            	logger.debug("과금로그 생성 성공");
            } else {
            	System.out.println("과금로그 생성 실패");
            	logger.debug("과금로그 생성 실패");
            }
        } catch (Exception e) {
        	System.out.println("과금로그 생성 실패 (좀 더 상세한 에러 원인 분석이 필요함)");
        	logger.debug(e);
        	System.out.println("과금로그 생성 실패 (좀 더 상세한 에러 원인 분석이 필요함)");
        	logger.debug(e);

        }

	}

	private void checkWeblogicAlive() {
		System.out.println("");
		System.out.println("################# Weblogic Alive 체크       #################");
		logger.debug("");
		logger.debug("################# Weblogic Alive 체크       #################");
		if (!getResultStreamRuntimeExec("/app/vmg/bin/COUNT-WAS").equals("0")) {
			System.out.println("Weblogic Alive 성공");
			logger.debug("Weblogic Alive 성공");
		} else {
			System.out.println("Weblogic Alive 실패");
			logger.debug("Weblogic Alive 실패");
		}
	}

	private void checkApacheAlive() {
		System.out.println("");
		System.out.println("################# Apache Alive 체크         #################");
		logger.debug("");
		logger.debug("################# Apache Alive 체크         #################");
		if (!getResultStreamRuntimeExec("/app/vmg/bin/COUNT-APACHE").equals("0")) {
			System.out.println("Apache Alive 성공");
			logger.debug("Apache Alive 성공");
		} else {
			System.out.println("Apache Alive 실패");
			logger.debug("Apache Alive 실패");
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
        	String message = "VMG 서버 전송테스트입니다 - " + CID;
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

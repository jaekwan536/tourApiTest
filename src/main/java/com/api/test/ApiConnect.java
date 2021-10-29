package com.api.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ApiConnect {

	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {

		// data 매개변수
		String title = "title";				// 제목
		String overview = "overview";		// 설명
		String addr = "addr1";				// 주소
		String x = "mapx";					// x
		String y = "mapy";					// y
		
		// 
	}

	private static List<Map<Integer, String>> getId() throws SAXException, IOException, ParserConfigurationException {

		List<Map<Integer, String>> list = new ArrayList<Map<Integer, String>>();
		int count = 0;
		String key = "";
		
		for (int i = 1; i < 48; i++) {
			String urlstr = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon?ServiceKey="
					+ "ServiceKey=" + key
					+ "&contentTypeId=12&areaCode=1&sigunguCode=&cat1=&cat2=&cat3=&listYN=Y"
					+ "&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&arrange=A&numOfRows=12&pageNo=" + i;

			Document documentInfo = null;
			documentInfo = (Document) DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(urlstr);
			documentInfo.getDocumentElement().normalize();

			Element root = documentInfo.getDocumentElement();
			NodeList nList = root.getElementsByTagName("items").item(0).getChildNodes();

			for (int j = 0; j < nList.getLength(); j++) {
				Map<Integer, String> map = new HashMap<Integer, String>();
				Node nNode = nList.item(j);
				Element eElement = (Element) nNode;

				map.put(count++, getTagValue("contentid", eElement));

				list.add(map);
			}
		}
		return list;
	}

	// detial > data 메소드 ( 제목, 주소, 설명, x, y )
	private static List<Map<Integer, String>> getData(String data) throws SAXException, IOException, ParserConfigurationException {

		List<Map<Integer, String>> list = new ArrayList<Map<Integer, String>>();
		int count = 0;
		String key = "";

		for (int i = 0; i < getId().size(); i++) {
			String urlstr = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon?"
					+ "ServiceKey=" + key
					+ "&contentTypeId=12&contentId=" + getId().get(i)
					+ "&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&defaultYN=Y"
					+ "&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y&transGuideYN=Y";

			Document documentInfo = null;
			documentInfo = (Document) DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(urlstr);
			documentInfo.getDocumentElement().normalize();

			Element root = documentInfo.getDocumentElement();
			NodeList nList = root.getElementsByTagName("items").item(0).getChildNodes();

			for (int j = 0; j < nList.getLength(); j++) {
				Map<Integer, String> map = new HashMap<Integer, String>();
				Node nNode = nList.item(j);
				Element eElement = (Element) nNode;

				map.put(count++, getTagValue(data, eElement));

				list.add(map);
			}
		}
		return list;
	}

	// 이미지 3개 가져오는 메소드
	private static List<Map<Integer, String>> getImage(String id) throws SAXException, IOException, ParserConfigurationException {

		List<Map<Integer, String>> list = new ArrayList<Map<Integer, String>>();
		int count = 0;
		String key = "";

		String urlstr = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailImage?"
				+ "ServiceKey=" + key
				+ "&contentTypeId=12&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&contentId=" + id + "&imageYN=Y";

		Document documentInfo = null;
		documentInfo = (Document) DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(urlstr);
		documentInfo.getDocumentElement().normalize();

		Element root = documentInfo.getDocumentElement();
		NodeList nList = root.getElementsByTagName("items").item(0).getChildNodes();

		for (int i = 0; i < 3; i++) {
			Map<Integer, String> map = new HashMap<Integer, String>();
			Node nNode = nList.item(i);
			Element eElement = (Element) nNode;

			map.put(count++, getTagValue("originimgurl", eElement));
			
			list.add(map);
		}
		return list;
	}

	private static String getTagValue(String tag, Element eElement) {
		NodeList nList = null;
		Node nValue = null;
		try {
			nList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
			nValue = (Node) nList.item(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (nValue == null)
			return null;
		return nValue.getNodeValue();
	}
}

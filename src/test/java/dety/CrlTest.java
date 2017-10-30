package dety;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Set;

public class CrlTest {
	public static void main(String[] args) throws FileNotFoundException, CertificateException, CRLException {

		FileInputStream fis = new FileInputStream("F:\\delta_crl.crl");
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		X509CRL aCrl = (X509CRL) cf.generateCRL(fis);

		int i = 0;
		Set<? extends X509CRLEntry> tSet = aCrl.getRevokedCertificates();
		Iterator<? extends X509CRLEntry> tIterator = tSet.iterator();
		while (tIterator.hasNext()) {
			X509CRLEntry tEntry = (X509CRLEntry) tIterator.next();
			String sn = tEntry.getSerialNumber().toString(16).toUpperCase();
			String issName = aCrl.getIssuerDN().toString();
			String time = new SimpleDateFormat("yyyyMMddHHmmss").format(tEntry.getRevocationDate());
			i++;
			System.out.println(i + "***************************");
			System.out.println("序列号：" + sn);
			System.out.println("颁发者：" + issName);
			System.out.println("注销时间：" + time);
			System.out.println(i + "***************************");
		}
		byte[] bagt = new byte[] {60, 82, 101, 113, 117, 101, 115, 116, 32, 120, 109, 108, 110, 115, 61, 34, 117, 114, 110, 58, 111, 97, 115, 105, 115, 58, 110, 97, 109, 101, 115, 58, 116, 99, 58, 83, 65, 77, 76, 58, 49, 46, 48, 58, 112, 114, 111, 116, 111, 99, 111, 108, 34, 32, 120, 109, 108, 110, 115, 58, 115, 97, 109, 108, 61, 34, 117, 114, 110, 58, 111, 97, 115, 105, 115, 58, 110, 97, 109, 101, 115, 58, 116, 99, 58, 83, 65, 77, 76, 58, 49, 46, 48, 58, 97, 115, 115, 101, 114, 116, 105, 111, 110, 34, 32, 120, 109, 108, 110, 115, 58, 115, 97, 109, 108, 112, 61, 34, 117, 114, 110, 58, 111, 97, 115, 105, 115, 58, 110, 97, 109, 101, 115, 58, 116, 99, 58, 83, 65, 77, 76, 58, 49, 46, 48, 58, 112, 114, 111, 116, 111, 99, 111, 108, 34, 32, 120, 109, 108, 110, 115, 58, 120, 115, 100, 61, 34, 104, 116, 116, 112, 58, 47, 47, 119, 119, 119, 46, 119, 51, 46, 111, 114, 103, 47, 50, 48, 48, 49, 47, 88, 77, 76, 83, 99, 104, 101, 109, 97, 34, 32, 120, 109, 108, 110, 115, 58, 120, 115, 105, 61, 34, 104, 116, 116, 112, 58, 47, 47, 119, 119, 119, 46, 119, 51, 46, 111, 114, 103, 47, 50, 48, 48, 49, 47, 88, 77, 76, 83, 99, 104, 101, 109, 97, 45, 105, 110, 115, 116, 97, 110, 99, 101, 34, 32, 73, 115, 115, 117, 101, 73, 110, 115, 116, 97, 110, 116, 61, 34, 50, 48, 49, 55, 45, 48, 53, 45, 49, 53, 84, 48, 51, 58, 48, 57, 58, 51, 53, 46, 52, 54, 52, 90, 34, 32, 77, 97, 106, 111, 114, 86, 101, 114, 115, 105, 111, 110, 61, 34, 49, 34, 32, 77, 105, 110, 111, 114, 86, 101, 114, 115, 105, 111, 110, 61, 34, 49, 34, 32, 82, 101, 113, 117, 101, 115, 116, 73, 68, 61, 34, 95, 54, 102, 52, 100, 49, 102, 52, 57, 99, 97, 97, 102, 100, 49, 97, 57, 51, 100, 101, 56, 102, 56, 50, 48, 99, 102, 52, 100, 100, 102, 97, 56, 34, 62, 60, 82, 101, 115, 112, 111, 110, 100, 87, 105, 116, 104, 62, 115, 97, 109, 108, 58, 65, 117, 116, 104, 101, 110, 116, 105, 99, 97, 116, 105, 111, 110, 83, 116, 97, 116, 101, 109, 101, 110, 116, 60, 47, 82, 101, 115, 112, 111, 110, 100, 87, 105, 116, 104, 62, 60, 65, 115, 115, 101, 114, 116, 105, 111, 110, 65, 114, 116, 105, 102, 97, 99, 116, 62, 65, 65, 70, 85, 112, 78, 65, 75, 122, 101, 114, 49, 89, 72, 67, 66, 111, 97, 108, 74, 119, 55, 83, 86, 84, 74, 78, 103, 97, 90, 79, 50, 82, 74, 100, 97, 51, 89, 56, 50, 109, 90, 48, 89, 114, 53, 74, 50, 52, 83, 70, 73, 116, 68, 80, 74, 60, 47, 65, 115, 115, 101, 114, 116, 105, 111, 110, 65, 114, 116, 105, 102, 97, 99, 116, 62, 60, 47, 82, 101, 113, 117, 101, 115, 116, 62};
		System.out.println(new String(bagt));
	}

}

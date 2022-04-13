
public class Code {

	public Code() {

	}

	public String decode(String s) {
		String code = "";
		for (int i = 0; i < s.length(); i += 3) {
			String part = s.substring(i, i + 3);
			int z = Integer.parseInt(part);
			z = z + 72;
			z = z / 5;
			char zeichen;
			zeichen = (char) z;
			code += zeichen;
		}
		return code;
	}

	public String encode(String s) {
		String code = "";
		for (int i = 0; i < s.length(); i++) {
			char charact = s.charAt(i);
			int ascii = (int) charact;
			ascii = ascii * 5 - 72;
			if (ascii < 100) {
				code += "0" + ascii;
			} else {
				code += ascii;
			}
		}
		return code;
	}

}

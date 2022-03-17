package witherixg;

import witherixg.annotations.Test;
import witherixg.annotations.Unfinished;

import java.util.*;
import java.util.regex.Pattern;

public class ObjectUtils{
	
	private ObjectUtils(){
	}
	
	public static void printObject(Object o, String... args){
		String text = toNormalClassName(o.getClass().getName());
		text += ":\n";
		int lns = 0;
		if(args.length > 0){
			int i = 0;
			Pattern p = Pattern.compile("ln/d");
			for(; i < args.length; i++){
				String s = args[i];
				try{
					p.matcher(s);
					break;
				}catch(Exception ignored){
				}
			}
			String integer = new StringBuffer(args[i]).delete(0, 2).toString();
			if(!"".equals(integer)){
				try{
					lns = Integer.parseInt(integer);
				}catch(Exception ignored){
				}
			}else{
				if(args[i].length() == 2){
					lns = Integer.MAX_VALUE;
				}
			}
		}
		text += toString(o, lns, 0);
		System.out.println(text);
	}
	
	private static String toString(Object o, int lns, int level){
		StringBuilder text = new StringBuilder();
		Class<?> objClass = o.getClass();
		if(o instanceof Collection){
			text.append(collectionObjectToString(o, lns, level));
		}else if(objClass.isArray()){
			text.append(arrayObjectToString(o, lns, level));
		}else if(o instanceof Map){
			text.append(mapObjectToString(o, lns, level));
		}else{
			return o.toString();
		}
		return text.toString();
	}
	
	private static String arrayObjectToString(Object arr, int lns, int level){
		if(arr == null){
			return "null";
		}else if(!arr.getClass().isArray()){
			return arr.toString();
		}else{
			StringBuilder sb = new StringBuilder(deepToString(new Object[]{arr}));
			sb.deleteCharAt(0);
			sb.deleteCharAt(sb.length() - 1);
			int count = 0;
			while(sb.charAt(count) == '['){
				count++;
			}
			if(lns > count){
				lns = count;
			}else if(lns < 0){
				lns = 0;
			}
			int depth = 0;
			int index = 0;
			while(index < sb.length()){
				if(sb.charAt(index) == '['){
					depth++;
				}else if(sb.charAt(index) == ']'){
					depth--;
				}
				
				if(depth <= lns && (sb.charAt(index) == '[' || (sb.charAt(index - 1) == ',' && sb.charAt(index) == ' '))){
					sb.insert(index + 1, "\n");
					index++;
				}
				if(index + 1 < sb.length() && index > 0){
					if(sb.charAt(index) == '\n'){
						for(int i = 0; i < depth; i++){
							sb.insert(index + 1, '\t');
							index++;
						}
					}else if(sb.charAt(index - 1) == '\n' && sb.charAt(index) == ']'){
						for(int i = 0; i < depth; i++){
							sb.insert(index, '\t');
							index++;
						}
					}
				}
				index++;
			}
			StringBuilder t = new StringBuilder();
			int next = Math.min(lns, level);
			t.append("\t".repeat(Math.max(0, next)));
			for(int i = 0; i < sb.length(); i++){
				if(sb.charAt(i) == '\n'){
					sb.insert(i + 1, t);
					i += next;
				}
			}
			sb.insert(0, t);
			return sb.toString();
		}
	}
	private static String collectionObjectToString(Object o, int lns, int level){
		Collection c = (Collection)o;
		Iterator it = c.iterator();
		if (!it.hasNext())
			return "{}";
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		while(it.hasNext()){
			Object e = it.next();
			sb.append(e.equals(c) ? "(this Collection)" : toString(e, lns, level + 1));
			if(it.hasNext()){
				sb.append(',').append(' ');
			}
		}
		sb.append('}');
		if(level == 0){
			int count = 0;
			while(sb.charAt(count) == '{'){
				count++;
			}
			if(lns > count){
				lns = count;
			}else if(lns < 0){
				lns = 0;
			}
			int depth = 0;
			int index = 0;
			while(index < sb.length()){
				if(sb.charAt(index) == '{'){
					depth++;
				}else if(sb.charAt(index) == '}'){
					depth--;
				}
				
				if(depth <= lns && (sb.charAt(index) == '{' || (sb.charAt(index - 1) == ',' && sb.charAt(index) == ' '))){
					sb.insert(index + 1, "\n");
					index++;
				}
				if(index + 1 < sb.length() && index > 0){
					if(sb.charAt(index) == '\n'){
						for(int i = 0; i < depth; i++){
							sb.insert(index + 1, '\t');
							index++;
						}
					}else if(sb.charAt(index - 1) == '\n' && sb.charAt(index) == '}'){
						for(int i = 0; i < depth; i++){
							sb.insert(index, '\t');
							index++;
						}
					}
				}
				index++;
			}
			StringBuilder t = new StringBuilder();
			int next = Math.min(lns, level);
			for(int i = 0; i < sb.length(); i++){
				if(sb.charAt(i) == '\n'){
					sb.insert(i + 1, t);
					i += next;
				}
			}
			sb.insert(0, t);
			if(level < lns){
				sb.insert(sb.length() - 1, '\n');
			}
		}
		return sb.toString();
	}
	private static String mapObjectToString(Object o, int lns, int level){
		Map m = (Map)o;
		Set s = m.keySet();
		Iterator it = s.iterator();
		if (!it.hasNext())
			return "{}";
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		while(it.hasNext()){
			Object e = it.next();
			sb.append(e.equals(m) ? "(this Map)" : toString(e, lns, level + 1)).append(" = ");
			sb.append(m.get(e).equals(m) ? "(this Map)" : toString(m.get(e), lns, level + 1));
			if(it.hasNext()){
				sb.append(',').append(' ');
			}
		}
		sb.append('}');
		int count = 0;
		while(sb.charAt(count) == '{'){
			count++;
		}
		if(lns > count){
			lns = count;
		}else if(lns < 0){
			lns = 0;
		}
		int depth = 0;
		int index = 0;
		while(index < sb.length()){
			if(sb.charAt(index) == '{'){
				depth++;
			}else if(sb.charAt(index) == '}'){
				depth--;
			}
			
			if(depth <= lns && (sb.charAt(index) == '{' || (sb.charAt(index - 1) == ',' && sb.charAt(index) == ' '))){
				sb.insert(index + 1, "\n");
				index++;
			}
			if(index + 1 < sb.length() && index > 0){
				if(sb.charAt(index) == '\n'){
					for(int i = 0; i < depth; i++){
						sb.insert(index + 1, '\t');
						index++;
					}
				}else if(sb.charAt(index - 1) == '\n' && sb.charAt(index) == '}'){
					for(int i = 0; i < depth; i++){
						sb.insert(index, '\t');
						index++;
					}
				}
			}
			index++;
		}
		StringBuilder t = new StringBuilder();
		int next = Math.min(lns, level);
		t.append("\t".repeat(Math.max(0, next)));
		for(int i = 0; i < sb.length(); i++){
			if(sb.charAt(i) == '\n'){
				sb.insert(i + 1, t);
				i += next;
			}
		}
		sb.insert(0, t);
		if(level < lns){
			sb.insert(sb.length() - 1, '\n');
		}
		return sb.toString();
	}
	
	private static boolean contain(String str, char... value){
		char[] arr = str.toCharArray();
		for(char s : arr){
			for(char c : value){
				if(s == c){
					return true;
				}
			}
		}
		return false;
	}
	
	private static String toNormalClassName(String className){
		char[] arr = className.toCharArray();
		char c;
		int bracket = 0;
		StringBuffer name = new StringBuffer(className);
		if(name.charAt(name.length() - 1) == ';'){
			name = new StringBuffer(name.toString()).deleteCharAt(name.length() - 1);
		}
		for(int i = 0; i < arr.length; i++){
			c = arr[i];
			if(i != arr.length - 1){
				if(c == '['){
					bracket += 1;
				}
			}
			if(i == arr.length - 1){
				if(!(contain((c + ""), 'B', 'S', 'I', 'J', 'F', 'D', 'Z', 'C'))){
					break;
				}else{
					switch(c){
						case 'B' -> name.append("byte");
						case 'S' -> name.append("short");
						case 'I' -> name.append("int");
						case 'J' -> name.append("long");
						case 'F' -> name.append("float");
						case 'D' -> name.append("double");
						case 'Z' -> name.append("boolean");
						case 'C' -> name.append("char");
					}
				}
			}
		}
		if(bracket > 0){
			name = new StringBuffer(name.toString()).delete(0, bracket + 1);
		}else{
			return name.toString();
		}
		while(bracket > 0){
			name.append("[]");
			bracket--;
		}
		return name.toString();
	}
	
	/**
	 * @see Arrays#deepToString(Object[])
	 */
	private static String deepToString(Object[] a) {
		if (a == null)
			return "null";
		
		int bufLen = 20 * a.length;
		if (a.length != 0 && bufLen <= 0)
			bufLen = Integer.MAX_VALUE;
		StringBuilder buf = new StringBuilder(bufLen);
		deepToString(a, buf, new HashSet<>());
		return buf.toString();
	}
	/**
	 * @see Arrays#deepToString(Object[], StringBuilder, Set)
	 */
	private static void deepToString(Object[] a, StringBuilder buf, Set<Object[]> dejaVu) {
		if (a == null) {
			buf.append("null");
			return;
		}
		int iMax = a.length - 1;
		if (iMax == -1) {
			buf.append("[]");
			return;
		}
		
		dejaVu.add(a);
		buf.append('[');
		for (int i = 0; ; i++) {
			
			Object element = a[i];
			if (element == null) {
				buf.append("null");
			} else {
				Class<?> eClass = element.getClass();
				
				if (eClass.isArray()) {
					if (eClass == byte[].class)
						buf.append(Arrays.toString((byte[]) element));
					else if (eClass == short[].class)
						buf.append(Arrays.toString((short[]) element));
					else if (eClass == int[].class)
						buf.append(Arrays.toString((int[]) element));
					else if (eClass == long[].class)
						buf.append(Arrays.toString((long[]) element));
					else if (eClass == char[].class)
						buf.append(Arrays.toString((char[]) element));
					else if (eClass == float[].class)
						buf.append(Arrays.toString((float[]) element));
					else if (eClass == double[].class)
						buf.append(Arrays.toString((double[]) element));
					else if (eClass == boolean[].class)
						buf.append(Arrays.toString((boolean[]) element));
					else { // element is an array of object references
						if (dejaVu.contains(element))
							buf.append("[...]");
						else
							deepToString((Object[])element, buf, dejaVu);
					}
				} else {  // element is non-null and not an array
					buf.append(toString(element, 0, 0));
				}
			}
			if (i == iMax)
				break;
			buf.append(", ");
		}
		buf.append(']');
		dejaVu.remove(a);
	}
	
	@Test
	public static void main(String[] args){
	
	}
	@Unfinished
	private final class Input{
		static final Scanner s = new Scanner(System.in);
		
	}
	@Unfinished
	private class Output{
	
	}
}

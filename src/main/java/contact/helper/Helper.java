package contact.helper;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class Helper {
	//final variables
	public final String passRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
	public final String emailRegex = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
	
	public List<String> split(String st, char breakPoint) {
		List<String> splitArr = new ArrayList<String>();
		String res = "";
		for(int i = 0; i < st.length(); ++i) {
			if(st.charAt(i) == breakPoint) {
				splitArr.add(res);
				res = "";
			}
			res += st.charAt(i);
		}
		if(res != "") splitArr.add(res);
		return splitArr;
	}
}

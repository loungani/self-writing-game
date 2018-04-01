package game;

import java.util.List;

public class UIResponse {
	private Option option;
	private List<String> args;
	
	public UIResponse(Option option, List<String> args) {
		this.option = option;
		this.args = args;
	}
	
	public Option getOption() {
		return option;
	}
	
	public List<String> getArgs() {
		return args;
	}

}

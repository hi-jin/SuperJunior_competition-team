package data;

import java.util.HashMap;

import javafx.scene.control.ListCell;

public class GroupCell extends ListCell<String>{
	private static HashMap<Integer, String> colorMap = new HashMap<>();
	
	public GroupCell() {
		colorMap.put(5, "-fx-background-color: #3d73ff;");
		colorMap.put(4, "-fx-background-color: #7096ff;");
		colorMap.put(3, "-fx-background-color: #8cabff;");
		colorMap.put(2, "-fx-background-color: #adc3ff;");
		colorMap.put(1, "-fx-background-color: #d1ddff;");
		colorMap.put(0, "-fx-background-color: #edf2ff;");
	}
	@Override
	protected void updateItem(String item, boolean empty) {
		// TODO Auto-generated method stub
		super.updateItem(item, empty);
		if(empty) {
			setText(null);
			setStyle("");
		} else {
			setText(item);
			setStyle("");
		}
	}

	@Override
	public void updateIndex(int index) {
		// TODO Auto-generated method stub
		super.updateIndex(index);
		String content = super.getItem();
		
		if(index%3==0) {
			setStyle(
					"-fx-font-size: 15.0;"
					+"-fx-border-color: black;"
					+"-fx-border-width: 0.5;"
					+"-fx-border-style: solid none none none;"
			);
		}
		else {
			if((index-2)%3==0 && index>1) {
				int percent = Integer.parseInt(content.split("%")[0])/20;
				setStyle(colorMap.get(percent)
						+"-fx-font-size: 15.0;"
						+"-fx-alignment: center;"
				);
			}else {
				setStyle(
						"-fx-font-size: 15.0;"
						+"-fx-alignment: center;"
				);
			}
		}
	}

}

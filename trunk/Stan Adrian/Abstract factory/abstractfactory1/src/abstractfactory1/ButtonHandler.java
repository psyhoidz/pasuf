package abstractfactory1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ButtonHandler implements ActionListener {
  main objAutoSearchUI;

  public void actionPerformed(ActionEvent e) {
    String searchResult = null;

    if (e.getActionCommand().equals(main.EXIT)) {
      System.exit(1);
    }
    if (e.getActionCommand().equals(main.SEARCH)) {
      //get input values
      String vhCategory = objAutoSearchUI.getSelectedCategory();
      String vhType = objAutoSearchUI.getSelectedType();

      //get one of Luxury or NonLuxury vehicle factories
      VehicleFactory vf = VehicleFactory.getVehicleFactory(vhCategory);

      if (vhType.equals(main.CAR)) {
        Car c = vf.getCar();
        searchResult = "Name: " + c.getCarName() + "  Features: "
            + c.getCarFeatures();
      }
      if (vhType.equals(main.SUV)) {
        SUV s = vf.getSUV();
        searchResult = "Name: " + s.getSUVName() + "  Features: "
            + s.getSUVFeatures();
      }
      objAutoSearchUI.setResult(searchResult);
    }

  }

  public ButtonHandler() {
  }

  public ButtonHandler(main inObjAutoSearchUI) {
    objAutoSearchUI = inObjAutoSearchUI;
  }

}
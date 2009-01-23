package abstractfactory3;

public class PC extends Computer {

   
    
public Parts getRAM() {
return new Parts("512 MB");
}

public Parts getProcessor() {
return new Parts("Celeron");
}


public Parts getMonitor() {
return new Parts("15 inches");
}
}// End of class
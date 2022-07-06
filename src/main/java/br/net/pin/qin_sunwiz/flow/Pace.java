package br.net.pin.qin_sunwiz.flow;

public interface Pace {

  public void log(String message);

  public void log(Throwable error);

  public void waitIfPausedAndThrowIfStopped() throws Exception;

}

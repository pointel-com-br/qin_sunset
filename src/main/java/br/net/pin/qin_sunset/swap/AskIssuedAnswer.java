package br.net.pin.qin_sunset.swap;

import com.google.gson.Gson;

public class AskIssuedAnswer {
  public Long createdAt;
  public String outLines;
  public String[] outLinesFrom;
  public Integer outLinesSize;
  public String errLines;
  public String[] errLinesFrom;
  public Integer errLinesSize;
  public Integer resultCode;
  public Boolean isDone;
  public Boolean hasOut;
  public Boolean hasErr;
  public Long finishedAt;

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  public static AskIssuedAnswer fromString(String json) {
    return new Gson().fromJson(json, AskIssuedAnswer.class);
  }
}

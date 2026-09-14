package com.ds.livetest;

import java.util.Arrays;
import org.springframework.boot.SpringApplication;

public class TestLiveTestApplication {

  public static void main(String[] args) {
    SpringApplication.from(LiveTestApplication::main)
        .with(TestcontainersConfiguration.class)
        .run(withLocalProfile(args));
  }

  private static String[] withLocalProfile(String[] args) {
    String[] runArgs = Arrays.copyOf(args, args.length + 1);
    runArgs[args.length] = "--spring.profiles.active=local";
    return runArgs;
  }
}

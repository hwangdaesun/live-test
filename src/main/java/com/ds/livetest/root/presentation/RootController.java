package com.ds.livetest.root.presentation;

import com.ds.livetest.generated.api.RootApi;
import com.ds.livetest.support.response.ApiEnvelope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController implements RootApi {

  @Override
  public ResponseEntity<ApiEnvelope> getRoot() {
    return ResponseEntity.ok(ApiEnvelope.ofSuccess("ok"));
  }

  @Override
  public ResponseEntity<ApiEnvelope> getHealth() {
    return ResponseEntity.ok(ApiEnvelope.ofSuccess("ok"));
  }
}

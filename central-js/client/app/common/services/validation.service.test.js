'use strict';

describe('ValidationService Tests', function() {

  var validationService;

  beforeEach(function(){
    module('app');
  });

  beforeEach(inject(function (_ValidationService_) {
    validationService = _ValidationService_;
  }));

  // describe('ValidationService', function(){

    it('Should be defined', function() {
      expect(validationService).toBeDefined();
    });

    it('Should be defined: ValidationService.validateByMarkers', function() {
      expect(validationService.validateByMarkers).toBeDefined();
    });

  // });
});
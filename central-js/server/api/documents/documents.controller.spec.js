'use strict';

var should = require('should');
var app = require('../../app');
var request = require('supertest');
require('../../auth/bankid/index.mock.js');

describe('POST /api/documents/initialUpload', function () {
    it('should respond with 200', function (done) {
        request(app).get('/auth/bankid/callback').expect(200).end(function () {
            request(app)
                .post('/api/service/documents/initialUpload')
                .send([{nID: 2, sName: 'Паспорт'}])
                .expect(200)
                .expect('Content-Type', /json/)
                .end(function (err, res) {
                    if (err) return done(err);

                    done();
                });
        });
    });
});
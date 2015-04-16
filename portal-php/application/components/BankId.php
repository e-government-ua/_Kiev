<?php

namespace app\components;

use yii\authclient\OAuth2;

class BankId extends OAuth2
{

    /**
     * @inheritdoc
     */
    public $authUrl = 'https://bankid.privatbank.ua/DataAccessService/das/authorize';

    /**
     * @inheritdoc
     */
    public $tokenUrl = 'https://bankid.privatbank.ua/DataAccessService/oauth/token';

    /**
     * @inheritdoc
     */
    public $apiBaseUrl = 'https://bankid.privatbank.ua/DataAccessService/checked';

    /**
     *
     * @return string
     */
    protected function defaultName()
    {
        return 'bankid';
    }

    /**
     *
     * @return string
     */
    protected function defaultTitle()
    {
        return 'Bank Id Ua';
    }

    /**
     *
     * @return array
     */
    protected function initUserAttributes()
    {
        $accessToken = $this->getAccessToken();
        $token = $accessToken->getToken();
        //$params['access_token'] = $token;
        //$params[] = '{"bankIdPhone":"true","bankIdLastName":"true","bankIdFirstName":"true","bankIdMiddleName":"true","bankIdAddress":"true","bankIdDocument":"true", "bankIdBirthDate":"true"}';

        $params = [
            json_encode($this->getRequestFields())
        ];
        $headers = [
            "Content-Type: application/json",
            "Authorization: Bearer $token",
            "Accept: application/json"
        ];

        $url = 'data?client_id=' . $this->clientId;
        return $this->api($url, 'CUSTOM_POST', $params, $headers);
    }

    /**
     * Composes HTTP request CUrl options, which will be merged with the default ones.
     * @param string $method request type.
     * @param string $url request URL.
     * @param array $params request params.
     * @return array CUrl options.
     * @throws Exception on failure.
     */
    protected function composeRequestCurlOptions($method, $url, array $params)
    {
        if ($method == 'CUSTOM_POST') {
            $curlOptions = [];
            $curlOptions[CURLOPT_POST] = true;
            $curlOptions[CURLOPT_POSTFIELDS] = implode('&', $params);
            return $curlOptions;
        } else {
            return parent::composeRequestCurlOptions($method, $url, $params);
        }
    }

    /**
     *
     */
    public function getAccessTokenWithoutRefresh()
    {
        $token = $this->getState('token');
        return $token;
    }

    /**
     *
     */
    public function getRequestFields()
    {
        return [
            "type"      => "physical",
            "fields"    => ["firstName", "middleName", "lastName", "phone", "inn", "clId", "birthDay"],
            //"fields"    => ["firstName", "middleName", "lastName", "phone", "inn", "clId", "clIdText", "birthDay"],
            "scans"     => [
                [
                    "type"   => "passport",
                    "fields" => ["link", "dateCreate"]
                ],
//                [
//                    "type"   => "zpassport",
//                    "fields" => ["link", "dateCreate"]
//                ],
//                [
//                    "type"   => "inn",
//                    "fields" => ["link", "dateCreate"]
//                ],
            ],
            "addresses" => [
                [
                    "type"   => "factual",
                    "fields" => ["country", "state", "area", "city", "street", "houseNo", "flatNo"],
                ],
//                [
//                    "type"   => "birth",
//                    "fields" => ["country", "state", "area", "city", "street", "houseNo", "flatNo"],
//                ],
            ],
            "documents" => [
                [
                    "type"   => "passport",
                    "fields" => ["series", "number", "issue", "dateIssue", "dateExpiration", "issueCountryIso2"],
                ],
//                [
//                    "type"   => "zpassport",
//                    "fields" => ["series", "number", "issue", "dateIssue", "dateExpiration", "issueCountryIso2"],
//                ],
            ],
        ];
    }

}

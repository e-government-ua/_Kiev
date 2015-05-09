<?php

namespace app\components;

use yii\authclient\OAuth2;

class BankId extends OAuth2
{

    /**
     * @inheritdoc
     */
    public $authUrl = 'https://bankid.org.ua/ResourceService/das/authorize';

    /**
     * @inheritdoc
     */
    public $tokenUrl = 'https://bankid.org.ua/ResourceService/oauth/token';

    /**
     * @inheritdoc
     */
    public $apiBaseUrl = 'https://bankid.org.ua/ResourceService/checked';

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

        $params = [
            json_encode($this->getRequestFields())
        ];
        $headers = [
            "Content-Type: application/json",
            "Authorization: Bearer $token, Id $this->clientId",
            "Accept: application/json"
        ];

        return $this->api('data', 'CUSTOM_POST', $params, $headers);
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
            "fields"    => ["firstName", "middleName", "lastName", "phone", "inn", "clId", "clIdText", "birthDay", "sex", "email"],
            "scans"     => [
                [
                    "type"   => "passport",
                    "fields" => ["link", "dateCreate"]
                ],
                [
                    "type"   => "zpassport",
                    "fields" => ["link", "dateCreate"]
                ],
                [
                    "type"   => "inn",
                    "fields" => ["link", "dateCreate"]
                ],
                [
                    "type"   => "personalPhoto",
                    "fields" => ["link", "dateCreate"]
                ],
            ],
            "addresses" => [
                [
                    "type"   => "factual",
                    "fields" => ["country", "state", "area", "city", "street", "houseNo", "flatNo"],
                ],
                [
                    "type"   => "birth",
                    "fields" => ["country", "state", "area", "city", "street", "houseNo", "flatNo"],
                ],
            ],
            "documents" => [
                [
                    "type"   => "passport",
                    "fields" => ["series", "number", "issue", "dateIssue", "dateExpiration", "issueCountryIso2"],
                ],
                [
                    "type"   => "zpassport",
                    "fields" => ["series", "number", "issue", "dateIssue", "dateExpiration", "issueCountryIso2"],
                ],
                [
                    "type"   => "ident",
                    "fields" => ["series", "number", "issue", "dateIssue", "dateExpiration", "issueCountryIso2"],
                ],
            ],
        ];
    }

}

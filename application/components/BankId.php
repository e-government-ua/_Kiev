<?php

namespace app\components;

use yii\authclient\OAuth2;

class BankId extends OAuth2
{

    /**
     * @inheritdoc
     */
    public $authUrl = 'https://bankid.org.ua/DataAccessService/das/authorize';

    /**
     * @inheritdoc
     */
    public $tokenUrl = 'https://bankid.org.ua/DataAccessService/oauth/token';

    /**
     * @inheritdoc
     */
    public $apiBaseUrl = 'https://bankid.org.ua/DataAccessService/checked';

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
        //return $this->api('testFio', 'GET');
        //return $this->api('allClientData', 'GET');
        $url = 'govData?client_id=' . $this->clientId;
        return $this->api($url, 'CUSTOM_POST');
    }

    /**
     * @inheritdoc
     */
    protected function apiInternal($accessToken, $url, $method, array $params, array $headers)
    {
        $token = $accessToken->getToken();
        //$params['access_token'] = $token;
        $params[] = '{"bankIdPhone":"true","bankIdLastName":"true","bankIdFirstName":"true","bankIdMiddleName":"true","bankIdAddress":"true","bankIdDocument":"true", "bankIdBirthDate":"true"}';

        $headers[] = "Content-Type: application/json";
        $headers[] = "Authorization: Bearer $token";
        $headers[] = "Accept: application/json";

        return $this->sendRequest($method, $url, $params, $headers);
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
        if($method == 'CUSTOM_POST') {
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

}

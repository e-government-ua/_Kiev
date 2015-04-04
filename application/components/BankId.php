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
        return $this->api('allClientData', 'GET');
    }

    /**
     * @inheritdoc
     */
    protected function apiInternal($accessToken, $url, $method, array $params, array $headers)
    {
        $token = $accessToken->getToken();
        $params['access_token'] = $token;
        $params['client_id'] = $this->clientId;
        $headers[] = "Authorization: Bearer $token";

        return $this->sendRequest($method, $url, $params, $headers);
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

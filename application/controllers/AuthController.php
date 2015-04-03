<?php

namespace app\controllers;

use app\models\User;
use Yii;
use yii\authclient\BaseOAuth;
use yii\web\Controller;

class AuthController extends Controller
{

    public function actions()
    {
        return [
            'login' => [
                'class' => 'yii\authclient\AuthAction',
                'successCallback' => [$this, 'successCallback'],
            ],
        ];
    }

    /**
     *
     * @param BaseOAuth $client
     */
    public function successCallback($client)
    {
        $tokenParams = $client->getAccessToken()->getParams();
        $attributes = $client->getUserAttributes();

        $user = new User;
        $user->id = 'userdata';
        $user->userData = $attributes['customer'];
        $user->accessToken = $tokenParams['access_token'];
        $user->refreshToken = $tokenParams['refresh_token'];
        $user->authKey = time() . rand(10000, 99999);
        $user->save();
        Yii::$app->user->login($user,  $tokenParams['expires_in']);
    }

    public function actionTest()
    {
        var_dump($_GET);
    }

}

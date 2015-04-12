<?php

namespace app\controllers;

use app\models\User;
use Yii;
use yii\authclient\BaseOAuth;
use yii\web\Controller;
use yii\web\NotFoundHttpException;

class AuthController extends Controller
{

    public function actions()
    {
        return [
            'authclient' => [
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

        if($attributes['state'] == 'err') {
            throw new NotFoundHttpException($attributes['desc']);
        }

        $user = new User;
        $user->id = 'userdata';
        $user->userData = $attributes['gov'];
        $user->accessToken = $tokenParams['access_token'];
        $user->refreshToken = $tokenParams['refresh_token'];
        $user->authKey = time() . rand(10000, 99999);
        $user->save();
        Yii::$app->user->login($user,  $tokenParams['expires_in']);
    }


    /**
     *
     */
    public function actionLogin()
    {
        return $this->render('login');
    }


}

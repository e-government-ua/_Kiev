<?php

namespace app\components;

use app\models\User as UserModel;
use Yii;
use yii\web\User as BaseWebUser;

/**
 *
 */
class User extends BaseWebUser
{
    protected $_model;

    /**
     * Загружаем пользователя
     * @return UserModel
     */
    public function loadUser()
    {
        if ($this->_model == null) {
            $this->_model = UserModel::findIdentity($this->id);
        }
        return $this->_model;
    }

    /**
     *
     */
    public function isTokenExpired()
    {
        $client = Yii::$app->authClientCollection->getClient('bankid');
        $token = $client->getAccessTokenWithoutRefresh();
        if (!is_object($token) || $token->getIsExpired()) {
            return true;
        }
        return false;
    }


}

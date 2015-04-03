<?php

namespace app\models;

use RecursiveArrayIterator;
use RecursiveIteratorIterator;
use Yii;
use yii\base\Object;
use yii\web\IdentityInterface;

class User extends Object implements IdentityInterface
{

    public $id;
    public $userData;
    public $accessToken;
    public $refreshToken;
    public $authKey;

    /**
     * @inheritdoc
     */
    public static function findIdentity($id)
    {
        $user = unserialize(Yii::$app->session->get($id));
        return $user ? $user : null;
    }

    /**
     * @inheritdoc
     */
    public static function findIdentityByAccessToken($token, $type = null)
    {
        return null;
    }

    /**
     * @inheritdoc
     */
    public function getId()
    {
        return $this->id;
    }

    /**
     * @inheritdoc
     */
    public function getAuthKey()
    {
        return $this->authKey;
    }

    /**
     * @inheritdoc
     */
    public function validateAuthKey($authKey)
    {
        return $this->authKey === $authKey;
    }

    /**
     * @inheritdoc
     */
    public function save()
    {
        $user = Yii::$app->session->set($this->getId(), serialize($this));
        return $user;
    }

    /**
     *
     */
    public function getUsername()
    {
        $dataArray = [$this->userData['lastName'], $this->userData['firstName'], $this->userData['middleName']];
        return implode(' ', $dataArray);
    }

    /**
     *
     * @param type $attribute
     * @return type
     */
    public function searchUserAttribute($attribute)
    {
        return $this->recursiveFind($this->userData, $attribute);
    }

    /**
     *
     * @param array $array
     * @param type $needle
     * @return type
     */
    public function recursiveFind(array $array, $needle)
    {
        $iterator = new RecursiveArrayIterator($array);
        $recursive = new RecursiveIteratorIterator($iterator, RecursiveIteratorIterator::SELF_FIRST);
        foreach ($recursive as $key => $value) {
            if ($key === $needle) {
                return $value;
            }
        }
    }

}

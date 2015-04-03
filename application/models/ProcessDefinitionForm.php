<?php

namespace app\models;

use app\components\ActiveField;
use Yii;
use yii\base\Model;
use yii\helpers\ArrayHelper;
use yii\jui\DatePicker;

class ProcessDefinitionForm extends Model
{

    const TYPE_STRING = 'string';
    const TYPE_LONG = 'long';
    const TYPE_ENUM = 'enum';
    const TYPE_DATE = 'date';

    /**
     * @var array
     */
    protected $_fields;

    /**
     * @var array
     */
    protected $_attributes;

    /**
     *
     * @param array $fields
     * @param array $defaultValues
     * @param array $config
     */
    public function __construct($fields, $config = [])
    {
        $attributesArray = [];
        $fieldsArray = [];

        foreach ($fields as $field) {
            $key = $field['id'];
            if($key == 'access_token') {
                continue;
            }
            $attributesArray[$key] = isset($field['value']) ? $field['value'] : '';
            $fieldsArray[$key] = $field;
        }
        $this->_attributes = $attributesArray;
        $this->_fields = $fieldsArray;

        $this->loadDefaultAtributes();

        parent::__construct($config);
    }

    /**
     * @inheritdoc
     */
    public function __get($name)
    {
        if (array_key_exists($name, $this->_attributes)) {
            return $this->_attributes[$name];
        } else {
            return parent::__get($name);
        }
    }

    /**
     * @inheritdoc
     */
    public function __set($name, $value)
    {
        if (array_key_exists($name, $this->_attributes)) {
            $this->_attributes[$name] = $value;
        } else {
            parent::__set($name, $value);
        }
    }

    /**
     * @inheritdoc
     */
    public function __isset($name)
    {
        if (array_key_exists($name, $this->_attributes)) {
            return isset($this->_attributes[$name]);
        } else {
            return parent::__isset($name);
        }
    }

    /**
     * @inheritdoc
     */
    public function __unset($name)
    {
        if (array_key_exists($name, $this->_attributes)) {
            unset($this->_attributes[$name]);
        } else {
            parent::__unset($name);
        }
    }

    /**
     * @inheritdoc
     */
    public function attributes()
    {
        return array_keys($this->_attributes);
    }

    /**
     *
     * @return array
     */
    public function attributeLabels()
    {
        $labels = [];
        foreach ($this->_fields as $key => $field) {
            $labels[$key] = $field['name'];
        }
        return $labels;
    }

    /**
     *
     * @return array
     */
    public function rules()
    {
        $validators = [];
        foreach ($this->_fields as $key => $field) {
            if($this->isAttributeFromBankid($key)) {
                continue;
            }
            if($field['required']) {
                $validators[] = [$key, 'required'];
            }
            switch ($field['type']) {
                case self::TYPE_DATE:
                    $validators[] = [$key, 'date', 'format' => $field['datePattern']];
                    break;
                case self::TYPE_ENUM:
                    $range = ArrayHelper::getColumn($field['enumValues'], 'id');
                    $validators[] = [$key, 'in', 'range' => $range];
                    break;
                case self::TYPE_STRING:
                    $validators[] = [$key, 'filter', 'filter' => 'trim'];
                    break;
                case self::TYPE_LONG:
                    $validators[] = [$key, 'number'];
                    break;
                default:
                    $validators[] = [$key, 'safe'];
            }
        }

        return $validators;
    }

    /**
     *
     * @param string $field
     * @return array
     */
    public function getField($field)
    {
        return isset($this->_fields[$field]) ? $this->_fields[$field] : null;
    }

    /**
     *
     * @param ActiveField
     * @return string
     */
    public function getEditField($key, $formField)
    {
        $field = $this->getField($key);
        if (!$field) {
            return;
        }

        if($this->isAttributeFromBankid($key)) {
            return $formField->plain();
        }

        switch ($field['type']) {
            case self::TYPE_DATE:
                $input = $formField->widget(DatePicker::className(), [
                    'options'       => ['class' => 'form-control'],
                    'dateFormat'    => $field['datePattern'],
                    'clientOptions' => [
                        'minDate' => 0,
                    ]
                ]);
                break;
            case self::TYPE_ENUM:
                $input = $formField->dropDownList(ArrayHelper::map($field['enumValues'], 'id', 'name'));
                break;
            case self::TYPE_STRING:
            case self::TYPE_LONG:
            default:
                $input = $formField->textInput();
        }
        return $input;
    }

    /**
     *
     * @return array
     */
    public function getAttributes()
    {
        return $this->_attributes;
    }


    /**
     *
     * @param string $attribute
     * @return boolean
     */
    public function isAttributeFromBankid($attribute)
    {
        if(strpos($attribute, 'cl') === 0) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param string $attribute
     * @return string
     */
    public function getAttributeValueFromBankid($attribute)
    {
        $attributeName = lcfirst(substr($attribute, 2));
        return Yii::$app->user->loadUser()->searchUserAttribute($attributeName);
    }

    /**
     *
     */
    public function loadDefaultAtributes()
    {
        $attributes = $this->_attributes;
        foreach($this->_attributes as $key => $value) {
            if($this->isAttributeFromBankid($key)) {
                $attributes[$key] = $this->getAttributeValueFromBankid($key);
            }
        }
        $this->_attributes = $attributes;
    }

    /**
     *
     * @return string
     */
    public function getFormEmail()
    {
        return isset($this->_attributes['email']) ? $this->_attributes['email'] : false;
    }

}

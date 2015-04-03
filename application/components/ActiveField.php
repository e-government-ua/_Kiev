<?php

namespace app\components;

use yii\bootstrap;
use yii\helpers\Html;

/**
 * ActiveField
 *
 * @author Rakovich Vladimir <rak.kture@gmail.com>
 * @since 2.0
 */
class ActiveField extends bootstrap\ActiveField
{

    /**
     *
     * @param array $options
     * @param string $value
     * @return $this
     */
    public function plain($options = [], $value = false)
    {
        if (!isset($options['class'])) {
            $options['class'] = 'plain';
        }
        $options = array_merge($this->inputOptions, $options);
        if ($value === false) {
            $value = $this->model->{$this->attribute};
        }
        $this->parts['{input}'] = Html::tag('div', $value, $options);

        return $this;
    }


}

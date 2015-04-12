<?php

use yii\helpers\Html;
use yii\web\View;
/* @var $this View */
$this->title = 'Портал -= Электронное правительство =-';
?>
<div class="site-index">
    <div class="body-content">
        <div class="row">
            <div class="col-lg-12 text-center">
                <ul>
                    <?php foreach($definitions as $definition): ?>
                        <h4>
                            <?= Html::a($definition['name'], ['/site/definition', 'id' => $definition['id']])?>
                        </h4>
                    <?php endforeach; ?>
                </ul>
            </div>
        </div>
    </div>
</div>

<?php

use yii\helpers\Html;
?>

<div class="text-center">
    <h1>Авторизація</h1>
    <div class="row">
        <div class="col-lg-12">
            <b>Ця послуга потребує Вашої веріфікації. Будь ласка, оберіть спосіб:</b>
            <br>
            <div class="row">
                <div class="well  col-lg-offset-3 col-lg-6">
                    <?= Html::a('Увійти через BankID', ['/auth/authclient', 'authclient' => 'bankid'], ['class' => 'btn btn-primary btn-lg btn-block']) ?> <br>
                    <?= Html::a('Скористатися електронно-цифровим підписом (згодом)', 'javascript:{}', ['class' => 'btn btn-default btn-lg btn-block']) ?>
                </div>
            </div>

        </div>
    </div>
</div>
# Generated by Django 3.1.6 on 2021-03-10 17:49

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('hospitals', '0002_hospital'),
    ]

    operations = [
        migrations.AddField(
            model_name='hospital',
            name='lat',
            field=models.DecimalField(decimal_places=10, default=0, max_digits=10),
        ),
        migrations.AddField(
            model_name='hospital',
            name='long',
            field=models.DecimalField(decimal_places=10, default=0, max_digits=10),
        ),
    ]

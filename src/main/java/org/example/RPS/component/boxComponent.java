package org.example.RPS.component;

import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.Required;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Required(HealthIntComponent.class)
public class boxComponent extends Component {
private HealthIntComponent hp;
private HealthIntComponent shield;
    private StringProperty name = new SimpleStringProperty();
    private StringProperty skill = new SimpleStringProperty();
    private StringProperty description = new SimpleStringProperty();

    private IntegerProperty level = new SimpleIntegerProperty();

    private IntegerProperty attack = new SimpleIntegerProperty();

    private BooleanBinding alive;

    private CharacterComponent data;

    public boxComponent(CharacterComponent characterComponent) {
        name.set(characterComponent.getName());
        skill.set(characterComponent.getSkillName());
        description.set(characterComponent.getDescription());

        level.set(characterComponent.getLevel());
        attack.set(characterComponent.getDamage());

        data = characterComponent;
    }

    @Override
    public void onAdded() {
        alive = hp.zeroProperty().not();
    }

    public CharacterComponent getData() {
        return data;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public HealthIntComponent getHp() {
        return hp;
    }
    public HealthIntComponent getShield() {
        return shield;
    }

    public int getLevel() {
        return level.get();
    }

    public IntegerProperty levelProperty() {
        return level;
    }

    public void setLevel(int level) {
        this.level.set(level);
    }

    public int getAttack() {
        return attack.get();
    }

    public IntegerProperty attackProperty() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack.set(attack);
    }
    public BooleanBinding aliveProperty() {
        return alive;
    }

    public boolean isAlive() {
        return alive.get();
    }

    public boolean isKO() {
        return !isAlive();
    }

    public StringProperty toStringProperty() {
        StringProperty prop = new SimpleStringProperty();

        prop.bind(hp.valueProperty().asString().concat(",").concat(shield.valueProperty().asString()).concat(",").concat(level).concat(",").concat(attack));

        return prop;
    }

    @Override
    public String toString() {
        return "Panel List{" +
                getName() +
                ", Hp = " + getHp() +
                ", Shield = " + getShield() +
                ", Level = " + getLevel() +
                ", Damage =" + getAttack() +
                '}';
    }
}

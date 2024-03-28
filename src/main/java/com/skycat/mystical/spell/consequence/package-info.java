/**
 * Contains all the consequences (effects) of spells.<br>
 * To create a new consequence based on a mixin, follow these steps:<br>
 * 1) Make the mixin<br>
 * 2) Create {@code MyConsequence extends SpellConsequence}<br>
 * 3) Create {@code MyConsequence.Factory extends ConsequenceFactory<MyConsequence>}<br>
 * 4) Fill out all the required methods<br>
 * 5) Add factory to {@link com.skycat.mystical.spell.Spells#consequenceFactories}<br>
 * 6) Add config options to {@link com.skycat.mystical.ConfigModel}<br>
 * To create a new consequence based on an event, follow these steps:<br>
 * 1) Create {@code MyConsequence extends SpellConsequence implements MyEvent}<br>
 * 2) Create {@code MyConsequence.Factory extends ConsequenceFactory<MyConsequence>}<br>
 * 3) Fill out all the required methods <br>
 * 4) Add factory to {@link com.skycat.mystical.spell.Spells#consequenceFactories}<br>
 * 5) Add config options to {@link com.skycat.mystical.ConfigModel}<br>
 * 6) Make sure that {@link com.skycat.mystical.spell.SpellHandler} {@code implements MyEvent}. If it does not:<br>
 * - Add handling logic (see {@link com.skycat.mystical.spell.SpellHandler#afterBlockBreak} for example).<br>
 * - Register in {@link com.skycat.mystical.Mystical#onWorldLoad}<br>
 * Hopefully future changes will streamline this process more.<br>
 */
package com.skycat.mystical.spell.consequence;